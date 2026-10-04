import { randomUUID } from 'node:crypto';
import pinoHttp from 'pino-http';
import { logger } from '../utils/logger.js';

// Gives every request an id (uses X-Request-Id from the caller if it looks safe).
// The id is returned in the response header and is written in every log line.
export const httpLogger = pinoHttp({
  logger,
  genReqId: (req, res) => {
    const incoming = req.headers['x-request-id'];
    const id = typeof incoming === 'string' && /^[\w-]{8,64}$/.test(incoming) ? incoming : randomUUID();
    res.setHeader('X-Request-Id', id);
    return id;
  },
  autoLogging: {
    ignore: (req) => req.url.startsWith('/health') || req.url === '/metrics',
  },
  customLogLevel: (req, res, err) => {
    if (err || res.statusCode >= 500) return 'error';
    if (res.statusCode >= 400) return 'warn';
    return 'info';
  },
});
