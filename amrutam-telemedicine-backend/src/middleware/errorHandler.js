import { AppError } from '../utils/errors.js';

export function notFoundHandler(req, res) {
  res.status(404).json({
    error: { code: 'ROUTE_NOT_FOUND', message: `No route for ${req.method} ${req.path}`, requestId: req.id },
  });
}

// Every error ends here. The client gets a clean JSON body; details stay in the logs.
export function errorHandler(err, req, res, next) {
  if (res.headersSent) return next(err);

  if (err instanceof AppError) {
    return res.status(err.statusCode).json({
      error: { code: err.code, message: err.message, details: err.details, requestId: req.id },
    });
  }

  // Bad JSON from the client
  if (err.type === 'entity.parse.failed') {
    return res.status(400).json({ error: { code: 'BAD_JSON', message: 'Body is not valid JSON', requestId: req.id } });
  }
  if (err.type === 'entity.too.large') {
    return res.status(413).json({ error: { code: 'BODY_TOO_LARGE', message: 'Body is too large', requestId: req.id } });
  }

  // PostgreSQL: unique rule broken
  if (err.code === '23505') {
    return res.status(409).json({ error: { code: 'CONFLICT', message: 'This record already exists', requestId: req.id } });
  }

  // Unknown error: log everything, tell the client nothing private
  req.log?.error({ err }, 'Unhandled error');
  res.status(500).json({ error: { code: 'INTERNAL_ERROR', message: 'Something went wrong', requestId: req.id } });
}
