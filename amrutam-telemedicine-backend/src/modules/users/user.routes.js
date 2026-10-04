import { Router } from 'express';
import { requireAuth } from '../../middleware/auth.js';
import { validate } from '../../middleware/validate.js';
import { updateProfileSchema } from './user.schemas.js';

export function createUserRouter({ userService }) {
  const router = Router();

  router.get('/me', requireAuth, async (req, res) => {
    res.json({ user: await userService.getMe(req.user.id) });
  });

  router.patch('/me', requireAuth, validate({ body: updateProfileSchema }), async (req, res) => {
    const ctx = { ip: req.ip, requestId: req.id, role: req.user.role };
    res.json({ user: await userService.updateMe(req.user.id, req.valid.body, ctx) });
  });

  return router;
}
