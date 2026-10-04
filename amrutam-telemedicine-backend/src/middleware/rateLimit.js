import rateLimit from 'express-rate-limit';
import { env } from '../config/env.js';

function makeLimiter(max, message) {
  return rateLimit({
    windowMs: env.RATE_LIMIT_WINDOW_SECONDS * 1000,
    limit: max,
    standardHeaders: 'draft-7',
    legacyHeaders: false,
    handler: (req, res) => {
      res.status(429).json({ error: { code: 'RATE_LIMITED', message, requestId: req.id } });
    },
  });
}

// For all API routes
export const apiLimiter = makeLimiter(env.RATE_LIMIT_MAX, 'Too many requests. Please slow down.');
// Stricter for login / register / MFA (stops password guessing)
export const authLimiter = makeLimiter(env.AUTH_RATE_LIMIT_MAX, 'Too many attempts. Please try again later.');
