import pg from 'pg';
import { env } from '../config/env.js';
import { logger } from '../utils/logger.js';

const pool = new pg.Pool({
  connectionString: env.DATABASE_URL,
  max: env.DB_POOL_MAX,
  idleTimeoutMillis: 30_000,
  connectionTimeoutMillis: 5_000,
  statement_timeout: 10_000, // stop any query that runs longer than 10 seconds
});

pool.on('error', (err) => logger.error({ err }, 'Unexpected database pool error'));

// Run a function inside one database transaction.
// If the function throws, everything is rolled back.
async function withTransaction(work) {
  const client = await pool.connect();
  try {
    await client.query('BEGIN');
    const result = await work(client);
    await client.query('COMMIT');
    return result;
  } catch (err) {
    await client.query('ROLLBACK').catch(() => {});
    throw err;
  } finally {
    client.release();
  }
}

// Services receive this small object (dependency injection), so tests can pass a fake one.
export const db = {
  query: (text, params) => pool.query(text, params),
  withTransaction,
  close: () => pool.end(),
};
