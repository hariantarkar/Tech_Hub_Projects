import { Router } from 'express';

export function createHealthRouter({ db }) {
  const router = Router();

  // "Is the process alive?"  (used by Docker / Kubernetes liveness check)
  router.get('/live', (req, res) => res.json({ status: 'ok' }));

  // "Can it serve traffic?"  (checks the database)
  router.get('/ready', async (req, res) => {
    let timer;
    try {
      await Promise.race([
        db.query('SELECT 1'),
        new Promise((_, reject) => {
          timer = setTimeout(() => reject(new Error('database timeout')), 2000);
        }),
      ]);
      res.json({ status: 'ready', database: 'up' });
    } catch (err) {
      req.log?.error({ err }, 'Readiness check failed');
      res.status(503).json({ status: 'not_ready', database: 'down' });
    } finally {
      clearTimeout(timer);
    }
  });

  return router;
}
