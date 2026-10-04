import { describe, it, expect } from 'vitest';
import jwt from 'jsonwebtoken';
import { signAccessToken, signMfaToken, verifyToken } from '../../src/utils/tokens.js';

describe('tokens', () => {
  it('signs and verifies an access token', () => {
    const token = signAccessToken({ userId: 'user-1', role: 'doctor', mfa: true });
    const payload = verifyToken(token, 'access');
    expect(payload.sub).toBe('user-1');
    expect(payload.role).toBe('doctor');
    expect(payload.mfa).toBe(true);
  });

  it('does not accept an MFA token as an access token', () => {
    const token = signMfaToken('user-1');
    expect(() => verifyToken(token, 'access')).toThrow();
  });

  it('rejects a token signed with another secret', () => {
    const fake = jwt.sign({ purpose: 'access', role: 'admin' }, 'another_secret_another_secret_12345', { subject: 'x', issuer: 'amrutam-api' });
    expect(() => verifyToken(fake, 'access')).toThrow();
  });

  it('rejects the "none" algorithm', () => {
    const fake = jwt.sign({ purpose: 'access', role: 'admin' }, '', { algorithm: 'none', subject: 'x', issuer: 'amrutam-api' });
    expect(() => verifyToken(fake, 'access')).toThrow();
  });
});
