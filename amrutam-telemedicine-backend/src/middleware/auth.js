import { verifyToken } from '../utils/tokens.js';
import { forbidden, unauthorized } from '../utils/errors.js';

// Reads "Authorization: Bearer <token>" and puts the user in req.user
export function requireAuth(req, res, next) {
  const header = req.get('authorization') || '';
  const [type, token] = header.split(' ');
  if (type !== 'Bearer' || !token) {
    return next(unauthorized('Missing access token'));
  }
  try {
    const payload = verifyToken(token, 'access');
    req.user = { id: payload.sub, role: payload.role, mfa: payload.mfa === true };
    next();
  } catch {
    next(unauthorized('Access token is invalid or expired', 'INVALID_TOKEN'));
  }
}

// RBAC: only the listed roles can pass.  Example: requireRole('admin', 'doctor')
export function requireRole(...allowedRoles) {
  return (req, res, next) => {
    if (!req.user) return next(unauthorized());
    if (!allowedRoles.includes(req.user.role)) {
      return next(forbidden('Your role cannot do this'));
    }
    next();
  };
}

// Admins must log in with MFA. Use this on every admin route.
export function requireMfaForAdmin(req, res, next) {
  if (req.user?.role === 'admin' && !req.user.mfa) {
    return next(forbidden('Admins must log in with MFA', 'MFA_REQUIRED'));
  }
  next();
}
