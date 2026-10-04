-- Amrutam telemedicine: first database version.
-- All core tables are created here so later stages only add code.

CREATE EXTENSION IF NOT EXISTS pg_trgm;     -- fast "contains" search on doctor names
CREATE EXTENSION IF NOT EXISTS btree_gist;  -- lets us block overlapping slots

-- ---------------------------------------------------------------
-- users: one row per login account
-- ---------------------------------------------------------------
CREATE TABLE users (
  id                   uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  email                text NOT NULL,
  password_hash        text NOT NULL,
  role                 text NOT NULL CHECK (role IN ('patient', 'doctor', 'admin')),
  status               text NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'suspended', 'deleted')),
  mfa_enabled          boolean NOT NULL DEFAULT false,
  mfa_secret_encrypted text,
  failed_login_count   integer NOT NULL DEFAULT 0,
  locked_until         timestamptz,
  last_login_at        timestamptz,
  created_at           timestamptz NOT NULL DEFAULT now(),
  updated_at           timestamptz NOT NULL DEFAULT now()
);
CREATE UNIQUE INDEX users_email_unique ON users (lower(email));
CREATE INDEX users_role_idx ON users (role);

-- ---------------------------------------------------------------
-- profiles: personal details (phone is encrypted)
-- ---------------------------------------------------------------
CREATE TABLE profiles (
  user_id         uuid PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
  full_name       text NOT NULL,
  phone_encrypted text,
  date_of_birth   date,
  gender          text CHECK (gender IN ('male', 'female', 'other')),
  created_at      timestamptz NOT NULL DEFAULT now(),
  updated_at      timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX profiles_name_trgm_idx ON profiles USING gin (full_name gin_trgm_ops);

-- ---------------------------------------------------------------
-- doctors: extra details for users whose role is doctor
-- ---------------------------------------------------------------
CREATE TABLE doctors (
  user_id             uuid PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
  specialization      text NOT NULL,
  license_number      text NOT NULL,
  years_of_experience integer NOT NULL DEFAULT 0 CHECK (years_of_experience >= 0),
  consultation_fee    numeric(10,2) NOT NULL DEFAULT 0 CHECK (consultation_fee >= 0),
  bio                 text,
  languages           text[] NOT NULL DEFAULT '{}',
  is_verified         boolean NOT NULL DEFAULT false,
  rating_avg          numeric(3,2) NOT NULL DEFAULT 0,
  created_at          timestamptz NOT NULL DEFAULT now(),
  updated_at          timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT doctors_license_number_unique UNIQUE (license_number)
);
CREATE INDEX doctors_specialization_idx ON doctors (specialization) WHERE is_verified;

-- ---------------------------------------------------------------
-- availability_slots: time windows a doctor can be booked
-- ---------------------------------------------------------------
CREATE TABLE availability_slots (
  id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  doctor_id  uuid NOT NULL REFERENCES doctors(user_id) ON DELETE CASCADE,
  start_time timestamptz NOT NULL,
  end_time   timestamptz NOT NULL,
  status     text NOT NULL DEFAULT 'open' CHECK (status IN ('open', 'booked', 'cancelled')),
  version    integer NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT slot_time_order CHECK (end_time > start_time),
  CONSTRAINT slot_same_start_unique UNIQUE (doctor_id, start_time),
  -- a doctor can never have two active slots that overlap in time
  CONSTRAINT slot_no_overlap EXCLUDE USING gist (
    doctor_id WITH =,
    tstzrange(start_time, end_time) WITH &&
  ) WHERE (status <> 'cancelled')
);
CREATE INDEX slots_open_idx ON availability_slots (doctor_id, start_time) WHERE status = 'open';

-- ---------------------------------------------------------------
-- consultations: a booking between one patient and one doctor
-- ---------------------------------------------------------------
CREATE TABLE consultations (
  id              uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  patient_id      uuid NOT NULL REFERENCES users(id),
  doctor_id       uuid NOT NULL REFERENCES doctors(user_id),
  slot_id         uuid NOT NULL REFERENCES availability_slots(id),
  status          text NOT NULL DEFAULT 'pending_payment'
                  CHECK (status IN ('pending_payment', 'booked', 'in_progress', 'completed', 'cancelled', 'no_show')),
  scheduled_start timestamptz NOT NULL,
  scheduled_end   timestamptz NOT NULL,
  reason          text,
  notes_encrypted text,
  started_at      timestamptz,
  ended_at        timestamptz,
  cancelled_at    timestamptz,
  cancel_reason   text,
  created_at      timestamptz NOT NULL DEFAULT now(),
  updated_at      timestamptz NOT NULL DEFAULT now()
);
-- Double booking safety: one active consultation per slot (cancelled ones do not count)
CREATE UNIQUE INDEX consultations_one_active_per_slot ON consultations (slot_id) WHERE status <> 'cancelled';
CREATE INDEX consultations_patient_idx ON consultations (patient_id, scheduled_start DESC);
CREATE INDEX consultations_doctor_idx  ON consultations (doctor_id, scheduled_start DESC);
CREATE INDEX consultations_status_idx  ON consultations (status, scheduled_start);

-- ---------------------------------------------------------------
-- prescriptions: written by the doctor after a consultation (encrypted)
-- ---------------------------------------------------------------
CREATE TABLE prescriptions (
  id                uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  consultation_id   uuid NOT NULL REFERENCES consultations(id),
  doctor_id         uuid NOT NULL REFERENCES doctors(user_id),
  patient_id        uuid NOT NULL REFERENCES users(id),
  content_encrypted text NOT NULL,
  created_at        timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX prescriptions_consultation_idx ON prescriptions (consultation_id);
CREATE INDEX prescriptions_patient_idx ON prescriptions (patient_id, created_at DESC);

-- ---------------------------------------------------------------
-- payments: one payment per consultation booking
-- ---------------------------------------------------------------
CREATE TABLE payments (
  id              uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  consultation_id uuid NOT NULL REFERENCES consultations(id),
  patient_id      uuid NOT NULL REFERENCES users(id),
  amount          numeric(10,2) NOT NULL CHECK (amount >= 0),
  currency        text NOT NULL DEFAULT 'INR',
  status          text NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'succeeded', 'failed', 'refunded')),
  provider        text NOT NULL DEFAULT 'mock',
  provider_ref    text,
  created_at      timestamptz NOT NULL DEFAULT now(),
  updated_at      timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX payments_consultation_idx ON payments (consultation_id);
CREATE INDEX payments_status_idx ON payments (status, created_at);

-- ---------------------------------------------------------------
-- audit_logs: who did what (append only, split by month)
-- ---------------------------------------------------------------
CREATE TABLE audit_logs (
  id          uuid NOT NULL DEFAULT gen_random_uuid(),
  actor_id    uuid,
  actor_role  text,
  action      text NOT NULL,
  entity_type text,
  entity_id   text,
  ip_address  text,
  request_id  text,
  details     jsonb NOT NULL DEFAULT '{}',
  created_at  timestamptz NOT NULL DEFAULT now(),
  PRIMARY KEY (id, created_at)
) PARTITION BY RANGE (created_at);

CREATE TABLE audit_logs_default PARTITION OF audit_logs DEFAULT;
CREATE INDEX audit_logs_actor_idx  ON audit_logs (actor_id, created_at DESC);
CREATE INDEX audit_logs_entity_idx ON audit_logs (entity_type, entity_id);
CREATE INDEX audit_logs_action_idx ON audit_logs (action, created_at DESC);

-- Nobody can change or delete an audit row (dropping an old month partition is still possible).
CREATE FUNCTION block_audit_change() RETURNS trigger AS $$
BEGIN
  RAISE EXCEPTION 'audit_logs is append-only';
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER audit_logs_no_change
  BEFORE UPDATE OR DELETE ON audit_logs
  FOR EACH ROW EXECUTE FUNCTION block_audit_change();

-- Helper: make the partition for one month.
CREATE FUNCTION create_audit_partition(p_year integer, p_month integer) RETURNS void AS $$
DECLARE
  start_date date := make_date(p_year, p_month, 1);
  end_date   date := (make_date(p_year, p_month, 1) + interval '1 month')::date;
  part_name  text := format('audit_logs_%s_%s', p_year, lpad(p_month::text, 2, '0'));
BEGIN
  EXECUTE format(
    'CREATE TABLE IF NOT EXISTS %I PARTITION OF audit_logs FOR VALUES FROM (%L) TO (%L)',
    part_name, start_date, end_date
  );
END;
$$ LANGUAGE plpgsql;

-- Make partitions for this month and the next 3 months.
DO $$
DECLARE
  m date;
BEGIN
  FOR i IN 0..3 LOOP
    m := (date_trunc('month', now()) + make_interval(months => i))::date;
    PERFORM create_audit_partition(extract(year FROM m)::int, extract(month FROM m)::int);
  END LOOP;
END;
$$;

-- ---------------------------------------------------------------
-- idempotency_keys: safe retries for write requests
-- ---------------------------------------------------------------
CREATE TABLE idempotency_keys (
  scope           text NOT NULL,          -- usually the user id
  key             text NOT NULL,          -- value of the Idempotency-Key header
  request_hash    text NOT NULL,          -- hash of method + path + body
  state           text NOT NULL DEFAULT 'processing' CHECK (state IN ('processing', 'done')),
  response_status integer,
  response_body   jsonb,
  created_at      timestamptz NOT NULL DEFAULT now(),
  expires_at      timestamptz NOT NULL DEFAULT now() + interval '24 hours',
  PRIMARY KEY (scope, key)
);
CREATE INDEX idempotency_expires_idx ON idempotency_keys (expires_at);

-- ---------------------------------------------------------------
-- jobs: background work queue (workers use FOR UPDATE SKIP LOCKED)
-- ---------------------------------------------------------------
CREATE TABLE jobs (
  id           bigserial PRIMARY KEY,
  type         text NOT NULL,
  payload      jsonb NOT NULL DEFAULT '{}',
  status       text NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'running', 'done', 'failed')),
  attempts     integer NOT NULL DEFAULT 0,
  max_attempts integer NOT NULL DEFAULT 5,
  run_at       timestamptz NOT NULL DEFAULT now(),
  last_error   text,
  created_at   timestamptz NOT NULL DEFAULT now(),
  updated_at   timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX jobs_ready_idx ON jobs (run_at) WHERE status = 'pending';
