import { defineConfig } from 'vitest/config';

// Vitest is the test tool from the Vite family.
// These values are only for tests (not real secrets).
export default defineConfig({
  test: {
    environment: 'node',
    include: ['tests/**/*.test.js'],
    testTimeout: 20000,
    env: {
      NODE_ENV: 'test',
      LOG_LEVEL: 'silent',
      DATABASE_URL: process.env.TEST_DATABASE_URL || 'postgres://postgres:postgres@localhost:5432/amrutam_test',
      JWT_SECRET: 'test_secret_test_secret_test_secret_123456',
      ENCRYPTION_KEYS: 'v1:/2cgX9SKRaSKv5qtbnwBhhLmE8Nqu64PuCVxFF43YKY=,v2:D96+N5bJbd8zZpZ4GnzKcO4UQiQUHFu4Iosj3PNmx2s=',
      ENCRYPTION_ACTIVE_KEY_ID: 'v2',
      BCRYPT_ROUNDS: '4',
      MAX_FAILED_LOGINS: '3',
      RATE_LIMIT_MAX: '1000',
      AUTH_RATE_LIMIT_MAX: '1000',
    },
  },
});
