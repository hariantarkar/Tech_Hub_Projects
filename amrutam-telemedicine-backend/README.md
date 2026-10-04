# Amrutam Telemedicine Backend

Backend API for a telemedicine system. Built with **Node.js (JavaScript)**, **Express**, and **PostgreSQL**.
This is an API only project. There is no frontend, because the assignment asks for a backend.

## Progress (3 stages)

| Stage | What is inside | Status |
|-------|----------------|--------|
| 1 | Project setup, full database, auth (roles + MFA), security basics, idempotency middleware, logs, metrics, health checks, Docker, tests | Done |
| 2 | Doctor slots, booking with payment saga, consultations, prescriptions, search, admin analytics, background jobs, caching | Next |
| 3 | OpenAPI file + Swagger page, OpenTelemetry tracing, Prometheus + Grafana, CI pipeline, architecture doc, threat model, backup and DR doc | After stage 2 |

## What you need

- Node.js 20 or newer (tested on Node 22)
- Docker Desktop **or** a local PostgreSQL 14+

## Setup

```bash
npm install
npm run setup:env        # makes a .env file with random secrets
```

### Option A: Docker (easiest)

```bash
docker compose up --build
```

The API runs at http://localhost:3000. The database tables are created automatically.

### Option B: Local PostgreSQL

1. Create an empty database called `amrutam` (and `amrutam_test` for tests).
2. Check `DATABASE_URL` inside `.env`.
3. Run:

```bash
npm run migrate          # creates the tables
npm run dev              # starts the server with auto restart
```

### Create the first admin

Admins cannot register from the public API (on purpose). Create one with:

```bash
npm run seed:admin -- admin@example.com StrongPass123
```

An admin must turn on MFA. Without MFA the admin token is blocked on admin routes.

## Tests

```bash
npm test                 # fast tests, no database needed
npm run test:db          # also runs the tests that use a real PostgreSQL (needs amrutam_test database)
```

Tests use **Vitest** (the test tool from the Vite family).

## Stage 1 API

Base path: `/api/v1`

| Method | Path | Who | What it does |
|--------|------|-----|--------------|
| POST | /auth/register | public | Register as `patient` or `doctor` |
| POST | /auth/login | public | Login. If MFA is on, returns `mfaToken` |
| POST | /auth/mfa/verify | public | Send `mfaToken` + 6 digit code, get `accessToken` |
| POST | /auth/mfa/setup | logged in | Get a secret + otpauth URL for an authenticator app |
| POST | /auth/mfa/enable | logged in | Send a 6 digit code to turn MFA on |
| GET | /users/me | logged in | My profile |
| PATCH | /users/me | logged in | Update name, phone, birth date, gender |

Other endpoints: `GET /health/live`, `GET /health/ready`, `GET /metrics`.

Try it:

```bash
curl -X POST http://localhost:3000/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"hari@example.com","password":"GoodPass123","fullName":"Hari Test"}'

curl -X POST http://localhost:3000/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"hari@example.com","password":"GoodPass123"}'

curl http://localhost:3000/api/v1/users/me -H "Authorization: Bearer <accessToken>"
```

Every error has the same shape:

```json
{ "error": { "code": "VALIDATION_ERROR", "message": "Some fields are not valid", "details": [], "requestId": "..." } }
```

## Folder map

```
src/
  server.js            starts the server, graceful shutdown
  app.js               builds the Express app (middleware + routes)
  container.js         connects services together (dependency injection)
  config/env.js        reads and checks environment variables
  db/                  pool, transaction helper, migration runner
  middleware/          auth (RBAC), validate, rateLimit, idempotency, metrics, errors, logger
  modules/
    auth/              register, login, MFA
    users/             profile
    audit/             audit log writer
  routes/              health routes
  utils/               crypto, password, tokens, totp, logger, errors
  scripts/             setupEnv, createAdmin
migrations/            SQL files (001_init.sql has ALL core tables)
tests/                 unit, api, integration
```

## Security and reliability features already in stage 1

- Passwords hashed with bcrypt; password rules; account lock after 5 wrong tries
- JWT access tokens (HS256 only, short life); MFA with TOTP codes
- RBAC: `requireRole(...)` and admin MFA check
- Sensitive fields (phone, MFA secret) encrypted with AES-256-GCM; key id is saved with the data so keys can be rotated
- Input validation with Zod (unknown fields are rejected)
- Rate limiting (stricter on login and register), Helmet security headers, body size limit
- Idempotency middleware for write requests (`Idempotency-Key` header)
- Audit log table is append only (a database trigger blocks update and delete) and split by month
- Database blocks double booking rules: no overlapping slots for one doctor, one active consultation per slot
- Structured JSON logs with request id and secret hiding; Prometheus metrics; health checks
- Secrets only from environment variables; Docker image runs as a non-root user

## Environment variables

See `.env.example`. The important ones are `DATABASE_URL`, `JWT_SECRET`, `ENCRYPTION_KEYS`, and `ENCRYPTION_ACTIVE_KEY_ID`.

### Rotating an encryption key

1. Make a new key: `node -e "console.log(require('crypto').randomBytes(32).toString('base64'))"`
2. Add it: `ENCRYPTION_KEYS=v1:oldKey,v2:newKey`
3. Set `ENCRYPTION_ACTIVE_KEY_ID=v2`
4. Old data is still readable. New data uses `v2`. A background job (stage 2) can re-encrypt old rows.
