import jwt from 'jsonwebtoken';
import { env } from '../config/env.js';

const ISSUER = 'amrutam-api';

// Two kinds of token:
//  - "access"    : used for normal API calls
//  - "mfa_login" : short token given after the password step, only good for the MFA code step
export function signAccessToken({ userId, role, mfa }) {
  return jwt.sign({ role, mfa, purpose: 'access' }, env.JWT_SECRET, {
    subject: userId,
    issuer: ISSUER,
    algorithm: 'HS256',
    expiresIn: env.ACCESS_TOKEN_MINUTES * 60,
  });
}

export function signMfaToken(userId) {
  return jwt.sign({ purpose: 'mfa_login' }, env.JWT_SECRET, {
    subject: userId,
    issuer: ISSUER,
    algorithm: 'HS256',
    expiresIn: 5 * 60,
  });
}

// Throws if the token is bad, expired, or made for another purpose.
export function verifyToken(token, expectedPurpose) {
  const payload = jwt.verify(token, env.JWT_SECRET, { issuer: ISSUER, algorithms: ['HS256'] });
  if (payload.purpose !== expectedPurpose) {
    throw new Error('Wrong token purpose');
  }
  return payload;
}
