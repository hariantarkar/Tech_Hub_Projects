import express from 'express';
import helmet from 'helmet';
import { env } from './config/env.js';
import { httpLogger } from './middleware/httpLogger.js';
import { metricsMiddleware, registry } from './middleware/metrics.js';
import { apiLimiter } from './middleware/rateLimit.js';
import { errorHandler, notFoundHandler } from './middleware/errorHandler.js';
import { createHealthRouter } from './routes/health.routes.js';
import { createAuthRouter } from './modules/auth/auth.routes.js';
import { createUserRouter } from './modules/users/user.routes.js';

// Mounts a router and remembers its base path, so metrics can label routes correctly.
function mount(app, basePath, router) {
  app.use(basePath, (req, res, next) => {
    res.locals.routeBase = basePath;
    next();
  }, router);
}

export function createApp(container) {
  const app = express();

  app.disable('x-powered-by');
  app.set('trust proxy', env.TRUST_PROXY);

  app.use(httpLogger);
  app.use(helmet());
  app.use(metricsMiddleware);
  app.use(express.json({ limit: '100kb' }));

  mount(app, '/health', createHealthRouter(container));

  app.get('/metrics', async (req, res) => {
    if (env.METRICS_TOKEN && req.get('authorization') !== `Bearer ${env.METRICS_TOKEN}`) {
      return res.status(401).json({ error: { code: 'UNAUTHORIZED', message: 'Metrics token needed' } });
    }
    res.set('Content-Type', registry.contentType);
    res.send(await registry.metrics());
  });

  app.use('/api/v1', apiLimiter);
  mount(app, '/api/v1/auth', createAuthRouter(container));
  mount(app, '/api/v1/users', createUserRouter(container));

  app.use(notFoundHandler);
  app.use(errorHandler);

  return app;
}
