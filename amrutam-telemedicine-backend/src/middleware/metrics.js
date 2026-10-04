import client from 'prom-client';

export const registry = new client.Registry();
client.collectDefaultMetrics({ register: registry });

export const httpDuration = new client.Histogram({
  name: 'http_request_duration_seconds',
  help: 'How long HTTP requests take',
  labelNames: ['method', 'route', 'status_code'],
  buckets: [0.01, 0.025, 0.05, 0.1, 0.2, 0.3, 0.5, 1, 2, 5],
  registers: [registry],
});

export const authEvents = new client.Counter({
  name: 'auth_events_total',
  help: 'Login and MFA results',
  labelNames: ['event'],
  registers: [registry],
});

export function metricsMiddleware(req, res, next) {
  const stopTimer = httpDuration.startTimer();
  res.on('finish', () => {
    // Use the route pattern (like /users/:id), not the real url, to keep label count small.
    const route = req.route ? `${res.locals.routeBase ?? ''}${req.route.path}` : 'unmatched';
    stopTimer({ method: req.method, route, status_code: res.statusCode });
  });
  next();
}
