import { logger } from '../../utils/logger.js';

// Audit trail: who did what, when, from where.
// Rows can only be added (the database blocks update and delete).
export function createAuditService({ db }) {
  // Use inside a transaction: pass the transaction client as the second argument.
  // If saving fails, the error is thrown, so the whole transaction is rolled back.
  async function record(event, executor = db) {
    await executor.query(
      `INSERT INTO audit_logs (actor_id, actor_role, action, entity_type, entity_id, ip_address, request_id, details)
       VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`,
      [
        event.actorId ?? null,
        event.actorRole ?? null,
        event.action,
        event.entityType ?? null,
        event.entityId ?? null,
        event.ip ?? null,
        event.requestId ?? null,
        JSON.stringify(event.details ?? {}),
      ],
    );
  }

  // For events where a logging problem must not break the user's request (like a failed login).
  async function recordSafe(event, executor = db) {
    try {
      await record(event, executor);
    } catch (err) {
      logger.error({ err, action: event.action }, 'Could not write audit log');
    }
  }

  return { record, recordSafe };
}
