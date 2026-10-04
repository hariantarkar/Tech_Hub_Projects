import { describe, it, expect, vi } from 'vitest';
import { requireAuth, requireRole, requireMfaForAdmin } from '../../src/middleware/auth.js';
import { signAccessToken } from '../../src/utils/tokens.js';

function run(middleware, req) {
  const next = vi.fn();
  middleware(req, {}, next);
  return next.mock.calls[0]?.[0]; // the error passed to next(), or undefined
}

describe('requireAuth', () => {
  it('rejects a request without a token', () => {
    const error = run(requireAuth, { get: () => '' });
    expect(error.statusCode).toBe(401);
  });

  it('accepts a good token and sets req.user', () => {
    const token = signAccessToken({ userId: 'u1', role: 'patient', mfa: false });
    const req = { get: () => `Bearer ${token}` };
    expect(run(requireAuth, req)).toBeUndefined();
    expect(req.user).toEqual({ id: 'u1', role: 'patient', mfa: false });
  });

  it('rejects a bad token', () => {
    expect(run(requireAuth, { get: () => 'Bearer not.a.token' }).statusCode).toBe(401);
  });
});

describe('requireRole', () => {
  it('lets the right role in', () => {
    expect(run(requireRole('doctor', 'admin'), { user: { role: 'doctor' } })).toBeUndefined();
  });

  it('blocks the wrong role with 403', () => {
    expect(run(requireRole('admin'), { user: { role: 'patient' } }).statusCode).toBe(403);
  });
});

describe('requireMfaForAdmin', () => {
  it('blocks an admin without MFA', () => {
    const error = run(requireMfaForAdmin, { user: { role: 'admin', mfa: false } });
    expect(error.statusCode).toBe(403);
    expect(error.code).toBe('MFA_REQUIRED');
  });

  it('lets an admin with MFA in', () => {
    expect(run(requireMfaForAdmin, { user: { role: 'admin', mfa: true } })).toBeUndefined();
  });

  it('does not affect other roles', () => {
    expect(run(requireMfaForAdmin, { user: { role: 'patient', mfa: false } })).toBeUndefined();
  });
});
