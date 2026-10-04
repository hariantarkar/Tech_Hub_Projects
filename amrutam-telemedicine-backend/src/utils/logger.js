import pino from 'pino';
import { env } from '../config/env.js';

// "redact" hides secrets if they ever end up in a log line.
export const logger = pino({
  level: env.LOG_LEVEL,
  base: { service: 'amrutam-api' },
  redact: {
    paths: [
      'req.headers.authorization',
      'req.headers.cookie',
      '*.password',
      '*.mfaToken',
      '*.code',
      '*.secret',
    ],
    censor: '[hidden]',
  },
});
