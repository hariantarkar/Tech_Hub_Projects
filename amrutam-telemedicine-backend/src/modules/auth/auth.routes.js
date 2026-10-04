import { Router } from 'express';
import { validate } from '../../middleware/validate.js';
import { requireAuth } from '../../middleware/auth.js';
import { authLimiter } from '../../middleware/rateLimit.js';
import { registerSchema, loginSchema, mfaVerifySchema, mfaEnableSchema } from './auth.schemas.js';

export function createAuthRouter({ authService }) {
  const router = Router();
  const ctxOf = (req) => ({ ip: req.ip, requestId: req.id });

  router.post('/register', authLimiter, validate({ body: registerSchema }), async (req, res) => {
    const user = await authService.register(req.valid.body, ctxOf(req));
    res.status(201).json({ user });
  });

  router.post('/login', authLimiter, validate({ body: loginSchema }), async (req, res) => {
    res.json(await authService.login(req.valid.body, ctxOf(req)));
  });

  router.post('/mfa/verify', authLimiter, validate({ body: mfaVerifySchema }), async (req, res) => {
    res.json(await authService.verifyMfaLogin(req.valid.body, ctxOf(req)));
  });

  router.post('/mfa/setup', requireAuth, async (req, res) => {
    res.json(await authService.setupMfa(req.user.id));
  });

  router.post('/mfa/enable', requireAuth, validate({ body: mfaEnableSchema }), async (req, res) => {
    res.json(await authService.enableMfa(req.user.id, req.valid.body, ctxOf(req)));
  });

  return router;
}
