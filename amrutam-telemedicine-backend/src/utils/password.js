import bcrypt from 'bcryptjs';
import { env } from '../config/env.js';

export function hashPassword(plainPassword) {
  return bcrypt.hash(plainPassword, env.BCRYPT_ROUNDS);
}

export function verifyPassword(plainPassword, hash) {
  return bcrypt.compare(plainPassword, hash);
}

// Used when the email does not exist, so the response time looks the same as a wrong password.
export const DUMMY_HASH = bcrypt.hashSync('not-a-real-password', env.BCRYPT_ROUNDS);
