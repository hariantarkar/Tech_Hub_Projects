import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath, pathToFileURL } from 'node:url';
import { logger } from '../utils/logger.js';

const migrationsFolder = fileURLToPath(new URL('../../migrations/', import.meta.url));
const LOCK_ID = 727274; // any number; stops two servers from migrating at the same time

export async function runMigrations(db) {
  await db.query(`
    CREATE TABLE IF NOT EXISTS schema_migrations (
      name       text PRIMARY KEY,
      applied_at timestamptz NOT NULL DEFAULT now()
    )
  `);

  const files = fs.readdirSync(migrationsFolder).filter((f) => f.endsWith('.sql')).sort();
  const applied = [];

  for (const file of files) {
    const didRun = await db.withTransaction(async (client) => {
      await client.query('SELECT pg_advisory_xact_lock($1)', [LOCK_ID]);
      const done = await client.query('SELECT 1 FROM schema_migrations WHERE name = $1', [file]);
      if (done.rowCount > 0) return false;

      await client.query(fs.readFileSync(path.join(migrationsFolder, file), 'utf8'));
      await client.query('INSERT INTO schema_migrations (name) VALUES ($1)', [file]);
      return true;
    });
    if (didRun) {
      applied.push(file);
      logger.info({ file }, 'Migration applied');
    }
  }
  return applied;
}

// Allows:  npm run migrate
if (process.argv[1] && import.meta.url === pathToFileURL(process.argv[1]).href) {
  const { db } = await import('./pool.js');
  try {
    const applied = await runMigrations(db);
    logger.info({ count: applied.length }, 'Migrations finished');
  } catch (err) {
    logger.error({ err }, 'Migration failed');
    process.exitCode = 1;
  } finally {
    await db.close();
  }
}
