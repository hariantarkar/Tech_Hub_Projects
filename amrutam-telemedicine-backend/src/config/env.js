import dotenv from 'dotenv';
import { z } from 'zod';

dotenv.config({ quiet: true }); // reads the .env file (real environment variables win)

const envSchema = z.object({
  NODE_ENV: z.enum(['development', 'test', 'production']).default('development'),
  PORT: z.coerce.number().int().positive().default(3000),
  LOG_LEVEL: z.enum(['fatal', 'error', 'warn', 'info', 'debug', 'trace', 'silent']).default('info'),

  DATABASE_URL: z.string().min(1),
  DB_POOL_MAX: z.coerce.number().int().positive().default(20),

  JWT_SECRET: z.string().min(32, 'JWT_SECRET must be at least 32 characters'),
  ACCESS_TOKEN_MINUTES: z.coerce.number().int().positive().default(30),

  ENCRYPTION_KEYS: z.string().min(1),
  ENCRYPTION_ACTIVE_KEY_ID: z.string().min(1).default('v1'),

  BCRYPT_ROUNDS: z.coerce.number().int().min(4).max(15).default(12),
  MAX_FAILED_LOGINS: z.coerce.number().int().positive().default(5),
  LOCK_MINUTES: z.coerce.number().int().positive().default(15),
  MFA_ISSUER: z.string().min(1).default('Amrutam'),

  RATE_LIMIT_WINDOW_SECONDS: z.coerce.number().int().positive().default(60),
  RATE_LIMIT_MAX: z.coerce.number().int().positive().default(300),
  AUTH_RATE_LIMIT_MAX: z.coerce.number().int().positive().default(10),
  TRUST_PROXY: z.coerce.number().int().min(0).default(0),

  METRICS_TOKEN: z.string().optional(),
});

const result = envSchema.safeParse(process.env);

if (!result.success) {
  console.error('Bad or missing environment variables:');
  for (const issue of result.error.issues) {
    console.error(` - ${issue.path.join('.')}: ${issue.message}`);
  }
  console.error('Tip: run "npm run setup:env" to create a .env file.');
  process.exit(1);
}

export const env = result.data;
