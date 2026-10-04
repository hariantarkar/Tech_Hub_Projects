import { authenticator } from 'otplib';
import { env } from '../config/env.js';

authenticator.options = { window: 1 }; // accept the previous and next 30s code (clock drift)

export const generateSecret = () => authenticator.generateSecret();
export const buildOtpAuthUrl = (email, secret) => authenticator.keyuri(email, env.MFA_ISSUER, secret);

export function checkCode(code, secret) {
  try {
    return authenticator.check(code, secret);
  } catch {
    return false;
  }
}
