import { describe, it, expect, vi } from 'vitest';
import { createIdempotency } from '../../src/middleware/idempotency.js';

// A tiny fake database that remembers idempotency rows in memory.
function makeFakeDb() {
  const rows = new Map();
  return {
    rows,
    query: vi.fn(async (sql, params) => {
      const [scope, key] = params;
      const id = `${scope}:${key}`;
      if (sql.startsWith('DELETE') && sql.includes('expires_at')) return { rowCount: 0 };
      if (sql.startsWith('DELETE')) { rows.delete(id); return { rowCount: 1 }; }
      if (sql.startsWith('INSERT')) {
        if (rows.has(id)) return { rowCount: 0, rows: [] };
        rows.set(id, { request_hash: params[2], state: 'processing' });
        return { rowCount: 1, rows: [{ key }] };
      }
      if (sql.startsWith('SELECT')) return { rows: rows.has(id) ? [rows.get(id)] : [] };
      if (sql.startsWith('UPDATE')) {
        Object.assign(rows.get(id), { state: 'done', response_status: params[2], response_body: JSON.parse(params[3]) });
        return { rowCount: 1 };
      }
      throw new Error(`unexpected sql: ${sql}`);
    }),
  };
}

function makeReqRes({ key = 'key-12345678', body = { a: 1 } } = {}) {
  const req = { method: 'POST', originalUrl: '/x', body, user: { id: 'u1' }, get: () => key };
  const res = {
    statusCode: 200,
    headers: {},
    set(name, value) { this.headers[name] = value; return this; },
    status(code) { this.statusCode = code; return this; },
    on() {},
    json(payload) { this.sent = payload; this.done?.(); return this; },
  };
  return { req, res };
}

async function callMiddleware(idempotent, { req, res }) {
  return new Promise((resolve) => {
    res.done = () => resolve({ next: false, res });
    idempotent(req, res, (err) => resolve({ next: true, err, res }));
  });
}

describe('idempotency middleware', () => {
  it('needs the Idempotency-Key header', async () => {
    const idempotent = createIdempotency({ db: makeFakeDb() });
    const result = await callMiddleware(idempotent, makeReqRes({ key: '' }));
    expect(result.err.code).toBe('IDEMPOTENCY_KEY_REQUIRED');
  });

  it('runs the handler the first time, then replays the saved answer', async () => {
    const db = makeFakeDb();
    const idempotent = createIdempotency({ db });

    // first request: handler runs, then sends an answer
    const first = makeReqRes();
    const firstResult = await callMiddleware(idempotent, first);
    expect(firstResult.next).toBe(true);
    await new Promise((resolve) => { first.res.done = resolve; first.res.status(201).json({ id: 'consultation-1' }); });

    // second request with the same key: the handler is NOT called, the answer is replayed
    const second = makeReqRes();
    const secondResult = await callMiddleware(idempotent, second);
    expect(secondResult.next).toBe(false);
    expect(second.res.statusCode).toBe(201);
    expect(second.res.sent).toEqual({ id: 'consultation-1' });
    expect(second.res.headers['Idempotent-Replay']).toBe('true');
  });

  it('returns 422 when the same key is used with a different body', async () => {
    const idempotent = createIdempotency({ db: makeFakeDb() });
    await callMiddleware(idempotent, makeReqRes({ body: { a: 1 } }));
    const result = await callMiddleware(idempotent, makeReqRes({ body: { a: 2 } }));
    expect(result.err.statusCode).toBe(422);
  });

  it('returns 409 while the first request is still running', async () => {
    const idempotent = createIdempotency({ db: makeFakeDb() });
    await callMiddleware(idempotent, makeReqRes());
    const result = await callMiddleware(idempotent, makeReqRes());
    expect(result.err.statusCode).toBe(409);
    expect(result.err.code).toBe('REQUEST_IN_PROGRESS');
  });
});
