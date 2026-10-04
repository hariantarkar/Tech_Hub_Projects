import { sha256 } from '../utils/crypto.js';
import { AppError, badRequest } from '../utils/errors.js';

// Idempotency = if the client sends the same write request twice (for example after a
// network timeout), the work is done only once and the same answer is returned.
//
// How to use on a route:   router.post('/', requireAuth, idempotent, handler)
// The client must send a header:   Idempotency-Key: <random text, 8-128 chars>
//
// Rules:
//  - same key + same request  -> first time runs the handler, later times replay the saved answer
//  - same key + different body -> 422 error (the key was reused by mistake)
//  - same key while the first request is still running -> 409 error
//  - if the handler fails with a 5xx error, the key is removed so the client can retry
export function createIdempotency({ db }) {
  return async function idempotent(req, res, next) {
    try {
      const key = req.get('idempotency-key');
      if (!key || key.length < 8 || key.length > 128) {
        throw badRequest('Idempotency-Key header is required (8 to 128 characters)', 'IDEMPOTENCY_KEY_REQUIRED');
      }

      const scope = req.user?.id ?? 'anonymous';
      const requestHash = sha256(`${req.method} ${req.originalUrl} ${JSON.stringify(req.body ?? {})}`);

      // Remove an old expired key with the same name, then try to claim the key.
      await db.query('DELETE FROM idempotency_keys WHERE scope = $1 AND key = $2 AND expires_at < now()', [scope, key]);
      const claim = await db.query(
        `INSERT INTO idempotency_keys (scope, key, request_hash)
         VALUES ($1, $2, $3)
         ON CONFLICT (scope, key) DO NOTHING
         RETURNING key`,
        [scope, key, requestHash],
      );

      if (claim.rowCount === 0) {
        const existing = (await db.query(
          'SELECT request_hash, state, response_status, response_body FROM idempotency_keys WHERE scope = $1 AND key = $2',
          [scope, key],
        )).rows[0];

        if (!existing) throw new AppError(409, 'IDEMPOTENCY_RETRY', 'Please retry the request');
        if (existing.request_hash !== requestHash) {
          throw new AppError(422, 'IDEMPOTENCY_KEY_REUSED', 'This Idempotency-Key was already used with a different request');
        }
        if (existing.state === 'processing') {
          throw new AppError(409, 'REQUEST_IN_PROGRESS', 'The first request with this key is still running');
        }
        res.set('Idempotent-Replay', 'true');
        return res.status(existing.response_status).json(existing.response_body);
      }

      // We own the key. Save the answer before it is sent to the client.
      const sendJson = res.json.bind(res);
      res.json = (body) => {
        const save = res.statusCode >= 500
          ? db.query('DELETE FROM idempotency_keys WHERE scope = $1 AND key = $2', [scope, key])
          : db.query(
              `UPDATE idempotency_keys SET state = 'done', response_status = $3, response_body = $4
               WHERE scope = $1 AND key = $2`,
              [scope, key, res.statusCode, JSON.stringify(body)],
            );
        save.catch((err) => req.log?.error({ err }, 'Could not save idempotency result')).finally(() => sendJson(body));
        return res;
      };

      // If the handler throws before sending, free the key so the client can retry.
      res.on('close', () => {
        if (!res.writableFinished) {
          db.query('DELETE FROM idempotency_keys WHERE scope = $1 AND key = $2 AND state = $3', [scope, key, 'processing']).catch(() => {});
        }
      });

      next();
    } catch (err) {
      next(err);
    }
  };
}
