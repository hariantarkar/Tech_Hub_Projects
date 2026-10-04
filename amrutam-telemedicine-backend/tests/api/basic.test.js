import { describe, it, expect } from 'vitest';
import request from 'supertest';
import { createApp } from '../../src/app.js';
import { buildContainer } from '../../src/container.js';

// These tests do not need a real database: we pass a fake one.
function makeApp({ dbWorks = true } = {}) {
  const db = {
    query: async () => {
      if (!dbWorks) throw new Error('db is down');
      return { rows: [], rowCount: 0 };
    },
    withTransaction: async () => { throw new Error('not used in these tests'); },
  };
  return createApp(buildContainer({ db }));
}

describe('health and metrics', () => {
  it('GET /health/live is ok', async () => {
    const res = await request(makeApp()).get('/health/live');
    expect(res.status).toBe(200);
    expect(res.body.status).toBe('ok');
  });

  it('GET /health/ready is 200 when the database works', async () => {
    const res = await request(makeApp()).get('/health/ready');
    expect(res.status).toBe(200);
  });

  it('GET /health/ready is 503 when the database is down', async () => {
    const res = await request(makeApp({ dbWorks: false })).get('/health/ready');
    expect(res.status).toBe(503);
  });

  it('GET /metrics gives Prometheus text', async () => {
    const res = await request(makeApp()).get('/metrics');
    expect(res.status).toBe(200);
    expect(res.text).toContain('http_request_duration_seconds');
  });
});

describe('basic API rules', () => {
  it('sends a request id header', async () => {
    const res = await request(makeApp()).get('/health/live');
    expect(res.headers['x-request-id']).toBeTruthy();
  });

  it('sets security headers (helmet)', async () => {
    const res = await request(makeApp()).get('/health/live');
    expect(res.headers['x-content-type-options']).toBe('nosniff');
    expect(res.headers['x-powered-by']).toBeUndefined();
  });

  it('returns 404 JSON for unknown routes', async () => {
    const res = await request(makeApp()).get('/nothing-here');
    expect(res.status).toBe(404);
    expect(res.body.error.code).toBe('ROUTE_NOT_FOUND');
  });

  it('returns 400 for bad JSON', async () => {
    const res = await request(makeApp()).post('/api/v1/auth/login').set('Content-Type', 'application/json').send('{bad json');
    expect(res.status).toBe(400);
    expect(res.body.error.code).toBe('BAD_JSON');
  });

  it('returns 400 with field details for a bad register body', async () => {
    const res = await request(makeApp()).post('/api/v1/auth/register').send({ email: 'nope', password: '1' });
    expect(res.status).toBe(400);
    expect(res.body.error.code).toBe('VALIDATION_ERROR');
    expect(res.body.error.details.length).toBeGreaterThan(0);
  });

  it('needs a token for /users/me', async () => {
    const res = await request(makeApp()).get('/api/v1/users/me');
    expect(res.status).toBe(401);
  });
});
