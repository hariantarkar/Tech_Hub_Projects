import { createAuditService } from './modules/audit/audit.service.js';
import { createAuthService } from './modules/auth/auth.service.js';
import { createUserService } from './modules/users/user.service.js';
import { createIdempotency } from './middleware/idempotency.js';

// Dependency injection: this is the one place where the parts are connected.
// Every service gets what it needs as an argument, so tests can pass a fake db.
export function buildContainer({ db }) {
  const auditService = createAuditService({ db });
  const authService = createAuthService({ db, auditService });
  const userService = createUserService({ db, auditService });
  const idempotent = createIdempotency({ db });

  return { db, auditService, authService, userService, idempotent };
}
