// Creates the first admin user. Two ways to run it:
//   1) put ADMIN_EMAIL and ADMIN_PASSWORD in your .env file, then:  npm run seed:admin
//   2) npm run seed:admin -- admin@example.com StrongPass123
// Admins cannot register through the public API on purpose.
import { db } from '../db/pool.js';
import { logger } from '../utils/logger.js';
import { hashPassword } from '../utils/password.js';
import { emailSchema, passwordSchema } from '../modules/auth/auth.schemas.js';

try {
  const email = emailSchema.parse(process.argv[2] ?? process.env.ADMIN_EMAIL);
  const password = passwordSchema.parse(process.argv[3] ?? process.env.ADMIN_PASSWORD);
  const passwordHash = await hashPassword(password);

  await db.withTransaction(async (client) => {
    const result = await client.query(
      `INSERT INTO users (email, password_hash, role) VALUES ($1, $2, 'admin')
       ON CONFLICT DO NOTHING RETURNING id`,
      [email, passwordHash],
    );
    if (result.rowCount === 0) {
      logger.warn('An account with this email already exists. Nothing changed.');
      return;
    }
    await client.query('INSERT INTO profiles (user_id, full_name) VALUES ($1, $2)', [result.rows[0].id, 'Admin']);
    await client.query(
      `INSERT INTO audit_logs (actor_id, actor_role, action, entity_type, entity_id)
       VALUES ($1, 'admin', 'admin.seeded', 'user', $2)`,
      [result.rows[0].id, String(result.rows[0].id)],
    );
    logger.info({ email }, 'Admin created. Turn on MFA after the first login.');
  });
} catch (err) {
  logger.error({ err: err.message }, 'Could not create admin (check ADMIN_EMAIL and ADMIN_PASSWORD)');
  process.exitCode = 1;
} finally {
  await db.close();
}
