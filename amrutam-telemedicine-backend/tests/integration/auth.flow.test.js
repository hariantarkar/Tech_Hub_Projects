import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import { authenticator } from 'otplib';
import { db } from '../../src/db/pool.js';
import { runMigrations } from '../../src/db/migrate.js';
import { buildContainer } from '../../src/container.js';
import { createApp } from '../../src/app.js';
import { hashPassword } from '../../src/utils/password.js';

// These tests use a REAL PostgreSQL database. Run them with:  npm run test:db
// (the database in TEST_DATABASE_URL must exist; tables are created and cleaned here)
const runDbTests = process.env.RUN_DB_TESTS === '1';

describe.runIf(runDbTests)('auth flow with real database', () => {
  let app;

  beforeAll(async () => {
    await runMigrations(db);
    await db.query('TRUNCATE users, audit_logs, idempotency_keys RESTART IDENTITY CASCADE');
    app = createApp(buildContainer({ db }));
  });

  afterAll(async () => {
    await db.close();
  });

  const patient = { email: 'patient@example.com', password: 'GoodPass123', fullName: 'Pat Ient', phone: '+919876543210' };

  it('registers a patient', async () => {
    const res = await request(app).post('/api/v1/auth/register').send(patient);
    expect(res.status).toBe(201);
    expect(res.body.user.role).toBe('patient');
    expect(res.body.user.password_hash).toBeUndefined();
  });

  it('does not allow the same email twice', async () => {
    const res = await request(app).post('/api/v1/auth/register').send({ ...patient, email: 'PATIENT@example.com' });
    expect(res.status).toBe(409);
    expect(res.body.error.code).toBe('EMAIL_TAKEN');
  });

  it('registers a doctor with doctor details', async () => {
    const res = await request(app).post('/api/v1/auth/register').send({
      email: 'doc@example.com', password: 'GoodPass123', fullName: 'Doc Tor',
      role: 'doctor', specialization: 'Ayurveda', licenseNumber: 'MH-1001', consultationFee: 500,
    });
    expect(res.status).toBe(201);

    const login = await request(app).post('/api/v1/auth/login').send({ email: 'doc@example.com', password: 'GoodPass123' });
    const me = await request(app).get('/api/v1/users/me').set('Authorization', `Bearer ${login.body.accessToken}`);
    expect(me.body.user.doctor.specialization).toBe('Ayurveda');
    expect(me.body.user.doctor.isVerified).toBe(false);
  });

  it('logs in and reads my profile', async () => {
    const login = await request(app).post('/api/v1/auth/login').send({ email: patient.email, password: patient.password });
    expect(login.status).toBe(200);
    expect(login.body.accessToken).toBeTruthy();

    const me = await request(app).get('/api/v1/users/me').set('Authorization', `Bearer ${login.body.accessToken}`);
    expect(me.status).toBe(200);
    expect(me.body.user.phone).toBe('+919876543210');
  });

  it('keeps the phone number encrypted in the database', async () => {
    const row = (await db.query("SELECT phone_encrypted FROM profiles p JOIN users u ON u.id = p.user_id WHERE u.email = 'patient@example.com'")).rows[0];
    expect(row.phone_encrypted).not.toContain('9876543210');
    expect(row.phone_encrypted.startsWith('v2:')).toBe(true);
  });

  it('updates the profile', async () => {
    const login = await request(app).post('/api/v1/auth/login').send({ email: patient.email, password: patient.password });
    const res = await request(app).patch('/api/v1/users/me').set('Authorization', `Bearer ${login.body.accessToken}`)
      .send({ gender: 'other', dateOfBirth: '1998-04-10' });
    expect(res.status).toBe(200);
    expect(res.body.user.gender).toBe('other');
    expect(res.body.user.dateOfBirth).toBe('1998-04-10');
  });

  it('writes audit logs and does not allow changing them', async () => {
    const count = (await db.query("SELECT count(*)::int AS n FROM audit_logs WHERE action = 'user.register'")).rows[0].n;
    expect(count).toBeGreaterThanOrEqual(2);
    await expect(db.query("UPDATE audit_logs SET action = 'hacked'")).rejects.toThrow(/append-only/);
    await expect(db.query('DELETE FROM audit_logs')).rejects.toThrow(/append-only/);
  });

  it('locks the account after too many wrong passwords', async () => {
    await request(app).post('/api/v1/auth/register').send({ email: 'lock@example.com', password: 'GoodPass123', fullName: 'Lock Me' });
    for (let i = 0; i < 3; i++) {
      const bad = await request(app).post('/api/v1/auth/login').send({ email: 'lock@example.com', password: 'WrongPass123' });
      expect(bad.status).toBe(401);
    }
    const locked = await request(app).post('/api/v1/auth/login').send({ email: 'lock@example.com', password: 'GoodPass123' });
    expect(locked.status).toBe(423);
    expect(locked.body.error.code).toBe('ACCOUNT_LOCKED');
  });

  it('runs the full MFA flow', async () => {
    await request(app).post('/api/v1/auth/register').send({ email: 'mfa@example.com', password: 'GoodPass123', fullName: 'Mfa User' });
    const login = await request(app).post('/api/v1/auth/login').send({ email: 'mfa@example.com', password: 'GoodPass123' });
    const auth = { Authorization: `Bearer ${login.body.accessToken}` };

    const setup = await request(app).post('/api/v1/auth/mfa/setup').set(auth);
    expect(setup.status).toBe(200);
    expect(setup.body.otpauthUrl).toContain('otpauth://totp/');

    const wrong = await request(app).post('/api/v1/auth/mfa/enable').set(auth).send({ code: '000000' });
    expect(wrong.status).toBe(401);

    const enable = await request(app).post('/api/v1/auth/mfa/enable').set(auth).send({ code: authenticator.generate(setup.body.secret) });
    expect(enable.status).toBe(200);

    // now the login needs a second step
    const step1 = await request(app).post('/api/v1/auth/login').send({ email: 'mfa@example.com', password: 'GoodPass123' });
    expect(step1.body.mfaRequired).toBe(true);
    expect(step1.body.accessToken).toBeUndefined();

    // the MFA token is not an access token
    const misuse = await request(app).get('/api/v1/users/me').set('Authorization', `Bearer ${step1.body.mfaToken}`);
    expect(misuse.status).toBe(401);

    const step2 = await request(app).post('/api/v1/auth/mfa/verify').send({ mfaToken: step1.body.mfaToken, code: authenticator.generate(setup.body.secret) });
    expect(step2.status).toBe(200);
    expect(step2.body.accessToken).toBeTruthy();
  });

  it('blocks an admin token that has no MFA', async () => {
    const { requireAuth, requireMfaForAdmin, requireRole } = await import('../../src/middleware/auth.js');
    const express = (await import('express')).default;
    const probe = express();
    probe.get('/admin-only', requireAuth, requireRole('admin'), requireMfaForAdmin, (req, res) => res.json({ ok: true }));
    probe.use((err, req, res, next) => res.status(err.statusCode || 500).json({ code: err.code }));

    await db.query(
      "INSERT INTO users (email, password_hash, role) VALUES ('admin@example.com', $1, 'admin')",
      [await hashPassword('GoodPass123')],
    );
    const login = await request(app).post('/api/v1/auth/login').send({ email: 'admin@example.com', password: 'GoodPass123' });
    const res = await request(probe).get('/admin-only').set('Authorization', `Bearer ${login.body.accessToken}`);
    expect(res.status).toBe(403);
    expect(res.body.code).toBe('MFA_REQUIRED');
  });

  it('blocks two active slots that overlap for the same doctor', async () => {
    const doctor = (await db.query("SELECT id FROM users WHERE email = 'doc@example.com'")).rows[0];
    await db.query("INSERT INTO availability_slots (doctor_id, start_time, end_time) VALUES ($1, '2030-01-01 10:00+00', '2030-01-01 10:30+00')", [doctor.id]);
    await expect(
      db.query("INSERT INTO availability_slots (doctor_id, start_time, end_time) VALUES ($1, '2030-01-01 10:15+00', '2030-01-01 10:45+00')", [doctor.id]),
    ).rejects.toThrow(/slot_no_overlap/);
  });
});
