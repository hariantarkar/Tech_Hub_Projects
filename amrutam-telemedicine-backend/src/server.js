import { env } from './config/env.js';
import { logger } from './utils/logger.js';
import { db } from './db/pool.js';
import { buildContainer } from './container.js';
import { createApp } from './app.js';

const container = buildContainer({ db });
const app = createApp(container);

const server = app.listen(env.PORT, () => {
  logger.info({ port: env.PORT, env: env.NODE_ENV }, 'API is running');
});

// Graceful shutdown: stop taking new requests, finish current ones, close the database.
let shuttingDown = false;
async function shutdown(signal) {
  if (shuttingDown) return;
  shuttingDown = true;
  logger.info({ signal }, 'Shutting down');

  const forceExit = setTimeout(() => process.exit(1), 10_000);
  forceExit.unref();

  server.close(async () => {
    await db.close();
    logger.info('Shutdown complete');
    process.exit(0);
  });
}

process.on('SIGTERM', () => shutdown('SIGTERM'));
process.on('SIGINT', () => shutdown('SIGINT'));
process.on('unhandledRejection', (err) => logger.error({ err }, 'Unhandled promise rejection'));
process.on('uncaughtException', (err) => {
  logger.fatal({ err }, 'Uncaught exception');
  process.exit(1);
});
