import { describe, it, expect } from 'vitest';
import { registerSchema, loginSchema } from '../../src/modules/auth/auth.schemas.js';
import { updateProfileSchema } from '../../src/modules/users/user.schemas.js';

const goodPatient = { email: 'Test@Example.com ', password: 'GoodPass123', fullName: 'Test User' };

describe('register schema', () => {
  it('accepts a patient and cleans the email', () => {
    const result = registerSchema.parse(goodPatient);
    expect(result.email).toBe('test@example.com');
    expect(result.role).toBe('patient');
  });

  it('rejects a weak password', () => {
    expect(registerSchema.safeParse({ ...goodPatient, password: 'short' }).success).toBe(false);
    expect(registerSchema.safeParse({ ...goodPatient, password: 'alllowercase123' }).success).toBe(false);
  });

  it('does not allow role admin', () => {
    expect(registerSchema.safeParse({ ...goodPatient, role: 'admin' }).success).toBe(false);
  });

  it('rejects unknown fields', () => {
    expect(registerSchema.safeParse({ ...goodPatient, isVerified: true }).success).toBe(false);
  });

  it('needs specialization and license for doctors', () => {
    expect(registerSchema.safeParse({ ...goodPatient, role: 'doctor' }).success).toBe(false);
    const ok = registerSchema.safeParse({ ...goodPatient, role: 'doctor', specialization: 'Ayurveda', licenseNumber: 'MH-12345' });
    expect(ok.success).toBe(true);
  });
});

describe('login schema', () => {
  it('needs email and password', () => {
    expect(loginSchema.safeParse({ email: 'a@b.com' }).success).toBe(false);
    expect(loginSchema.safeParse({ email: 'a@b.com', password: 'x' }).success).toBe(true);
  });
});

describe('profile update schema', () => {
  it('needs at least one field', () => {
    expect(updateProfileSchema.safeParse({}).success).toBe(false);
  });

  it('rejects a future birth date and a fake date', () => {
    expect(updateProfileSchema.safeParse({ dateOfBirth: '2999-01-01' }).success).toBe(false);
    expect(updateProfileSchema.safeParse({ dateOfBirth: '2000-02-31' }).success).toBe(false);
    expect(updateProfileSchema.safeParse({ dateOfBirth: '1999-05-20' }).success).toBe(true);
  });
});
