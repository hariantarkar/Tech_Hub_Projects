import { defineConfig, mergeConfig } from 'vitest/config';
import baseConfig from './vitest.config.js';

// Same as vitest.config.js, but also runs the tests that need a real PostgreSQL database.
// (Written this way so that "npm run test:db" works on Windows, Mac and Linux.)
export default mergeConfig(
  baseConfig,
  defineConfig({ test: { env: { RUN_DB_TESTS: '1' } } }),
);
