import { env } from '../../config/env.js';
import { encryptText, decryptText } from '../../utils/crypto.js';
import { hashPassword, verifyPassword, DUMMY_HASH } from '../../utils/password.js';
import { signAccessToken, signMfaToken, verifyToken } from '../../utils/tokens.js';
import { generateSecret, buildOtpAuthUrl, checkCode } from '../../utils/totp.js';
import { AppError, badRequest, conflict, forbidden, unauthorized } from '../../utils/errors.js';
import { authEvents } from '../../middleware/metrics.js';

export function createAuthService({ db, auditService }) {
  const wrongLogin = () => unauthorized('Email or password is wrong', 'INVALID_CREDENTIALS');
  const lockedError = () =>
    new AppError(423, 'ACCOUNT_LOCKED', 'Too many failed attempts. Please try again later.');

  // ---------- register ----------
  async function register(input, ctx) {
    const passwordHash = await hashPassword(input.password);

    try {
      return await db.withTransaction(async (client) => {
        const userResult = await client.query(
          `INSERT INTO users (email, password_hash, role)
           VALUES ($1, $2, $3)
           RETURNING id, email, role, created_at`,
          [input.email, passwordHash, input.role],
        );
        const user = userResult.rows[0];

        await client.query(
          'INSERT INTO profiles (user_id, full_name, phone_encrypted) VALUES ($1, $2, $3)',
          [user.id, input.fullName, input.phone ? encryptText(input.phone) : null],
        );

        if (input.role === 'doctor') {
          await client.query(
            `INSERT INTO doctors (user_id, specialization, license_number, years_of_experience, consultation_fee)
             VALUES ($1, $2, $3, $4, $5)`,
            [user.id, input.specialization, input.licenseNumber, input.yearsOfExperience ?? 0, input.consultationFee ?? 0],
          );
        }

        await auditService.record(
          { actorId: user.id, actorRole: user.role, action: 'user.register', entityType: 'user', entityId: user.id, ...ctx },
          client,
        );

        return { id: user.id, email: user.email, role: user.role, createdAt: user.created_at };
      });
    } catch (err) {
      if (err.code === '23505' && err.constraint === 'users_email_unique') {
        throw conflict('This email is already registered', 'EMAIL_TAKEN');
      }
      if (err.code === '23505' && err.constraint === 'doctors_license_number_unique') {
        throw conflict('This license number is already registered', 'LICENSE_TAKEN');
      }
      throw err;
    }
  }

  // ---------- login (step 1: password) ----------
  async function login({ email, password }, ctx) {
    const result = await db.query(
      `SELECT id, email, role, password_hash, status, mfa_enabled, locked_until
       FROM users WHERE lower(email) = $1`,
      [email],
    );
    const user = result.rows[0];

    if (!user) {
      await verifyPassword(password, DUMMY_HASH); // keeps the timing similar
      authEvents.inc({ event: 'login_failed' });
      await auditService.recordSafe({ action: 'auth.login_failed', details: { reason: 'unknown_user' }, ...ctx });
      throw wrongLogin();
    }

    if (user.locked_until && user.locked_until > new Date()) {
      authEvents.inc({ event: 'login_locked' });
      await auditService.recordSafe({ actorId: user.id, actorRole: user.role, action: 'auth.login_blocked', details: { reason: 'locked' }, ...ctx });
      throw lockedError();
    }

    const passwordOk = await verifyPassword(password, user.password_hash);
    if (!passwordOk) {
      await addFailedAttempt(user.id);
      authEvents.inc({ event: 'login_failed' });
      await auditService.recordSafe({ actorId: user.id, actorRole: user.role, action: 'auth.login_failed', details: { reason: 'bad_password' }, ...ctx });
      throw wrongLogin();
    }

    if (user.status !== 'active') {
      throw forbidden('This account is not active', 'ACCOUNT_INACTIVE');
    }

    // Password is right. If MFA is on, the user still needs the second step.
    if (user.mfa_enabled) {
      return { mfaRequired: true, mfaToken: signMfaToken(user.id), mfaTokenExpiresIn: 300 };
    }

    await markLoginSuccess(user.id);
    authEvents.inc({ event: 'login_ok' });
    await auditService.recordSafe({ actorId: user.id, actorRole: user.role, action: 'auth.login', details: { mfa: false }, ...ctx });
    return buildLoginResponse(user, false);
  }

  // ---------- login (step 2: MFA code) ----------
  async function verifyMfaLogin({ mfaToken, code }, ctx) {
    let payload;
    try {
      payload = verifyToken(mfaToken, 'mfa_login');
    } catch {
      throw unauthorized('MFA token is invalid or expired', 'INVALID_TOKEN');
    }

    const result = await db.query(
      `SELECT id, email, role, status, mfa_enabled, mfa_secret_encrypted, locked_until
       FROM users WHERE id = $1`,
      [payload.sub],
    );
    const user = result.rows[0];
    if (!user || !user.mfa_enabled || !user.mfa_secret_encrypted) throw unauthorized('MFA is not active', 'INVALID_TOKEN');
    if (user.status !== 'active') throw forbidden('This account is not active', 'ACCOUNT_INACTIVE');
    if (user.locked_until && user.locked_until > new Date()) throw lockedError();

    if (!checkCode(code, decryptText(user.mfa_secret_encrypted))) {
      await addFailedAttempt(user.id);
      authEvents.inc({ event: 'mfa_failed' });
      await auditService.recordSafe({ actorId: user.id, actorRole: user.role, action: 'auth.mfa_failed', ...ctx });
      throw unauthorized('The code is wrong', 'INVALID_MFA_CODE');
    }

    await markLoginSuccess(user.id);
    authEvents.inc({ event: 'login_ok' });
    await auditService.recordSafe({ actorId: user.id, actorRole: user.role, action: 'auth.login', details: { mfa: true }, ...ctx });
    return buildLoginResponse(user, true);
  }

  // ---------- MFA setup ----------
  async function setupMfa(userId) {
    const user = (await db.query('SELECT email, mfa_enabled FROM users WHERE id = $1', [userId])).rows[0];
    if (!user) throw unauthorized();
    if (user.mfa_enabled) throw conflict('MFA is already turned on', 'MFA_ALREADY_ENABLED');

    const secret = generateSecret();
    await db.query('UPDATE users SET mfa_secret_encrypted = $2, updated_at = now() WHERE id = $1', [userId, encryptText(secret)]);
    return { secret, otpauthUrl: buildOtpAuthUrl(user.email, secret) };
  }

  async function enableMfa(userId, { code }, ctx) {
    const user = (await db.query('SELECT role, mfa_enabled, mfa_secret_encrypted FROM users WHERE id = $1', [userId])).rows[0];
    if (!user) throw unauthorized();
    if (user.mfa_enabled) throw conflict('MFA is already turned on', 'MFA_ALREADY_ENABLED');
    if (!user.mfa_secret_encrypted) throw badRequest('Call POST /auth/mfa/setup first', 'MFA_NOT_SET_UP');

    if (!checkCode(code, decryptText(user.mfa_secret_encrypted))) {
      throw unauthorized('The code is wrong', 'INVALID_MFA_CODE');
    }

    await db.query('UPDATE users SET mfa_enabled = true, updated_at = now() WHERE id = $1', [userId]);
    await auditService.recordSafe({ actorId: userId, actorRole: user.role, action: 'auth.mfa_enabled', entityType: 'user', entityId: userId, ...ctx });
    return { mfaEnabled: true, message: 'MFA is on. Log in again to get a token with MFA.' };
  }

  // ---------- small helpers ----------
  // One SQL statement, so two parallel wrong passwords cannot skip the counter.
  async function addFailedAttempt(userId) {
    await db.query(
      `UPDATE users SET
         locked_until = CASE WHEN failed_login_count + 1 >= $2 THEN now() + make_interval(mins => $3) ELSE locked_until END,
         failed_login_count = CASE WHEN failed_login_count + 1 >= $2 THEN 0 ELSE failed_login_count + 1 END,
         updated_at = now()
       WHERE id = $1`,
      [userId, env.MAX_FAILED_LOGINS, env.LOCK_MINUTES],
    );
  }

  async function markLoginSuccess(userId) {
    await db.query(
      'UPDATE users SET failed_login_count = 0, locked_until = NULL, last_login_at = now() WHERE id = $1',
      [userId],
    );
  }

  function buildLoginResponse(user, usedMfa) {
    return {
      accessToken: signAccessToken({ userId: user.id, role: user.role, mfa: usedMfa }),
      tokenType: 'Bearer',
      expiresIn: env.ACCESS_TOKEN_MINUTES * 60,
      user: { id: user.id, email: user.email, role: user.role },
    };
  }

  return { register, login, verifyMfaLogin, setupMfa, enableMfa };
}
