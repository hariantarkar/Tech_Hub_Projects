import { z } from 'zod';

const email = z.string().trim().toLowerCase().email('Email is not valid').max(254);

// bcrypt only reads the first 72 bytes, so we cap the size.
const password = z
  .string()
  .min(10, 'Password must be at least 10 characters')
  .refine((value) => Buffer.byteLength(value) <= 72, 'Password is too long (max 72 bytes)')
  .refine((value) => /[a-z]/.test(value), 'Password needs a lowercase letter')
  .refine((value) => /[A-Z]/.test(value), 'Password needs an uppercase letter')
  .refine((value) => /[0-9]/.test(value), 'Password needs a number');

const code = z.string().regex(/^\d{6}$/, 'Code must be 6 digits');

export const registerSchema = z
  .object({
    email,
    password,
    fullName: z.string().trim().min(2).max(100),
    phone: z.string().trim().regex(/^\+?[0-9]{10,15}$/, 'Phone must have 10 to 15 digits').optional(),
    role: z.enum(['patient', 'doctor']).default('patient'), // "admin" can never be self-registered
    // doctor only fields
    specialization: z.string().trim().min(2).max(100).optional(),
    licenseNumber: z.string().trim().min(3).max(50).optional(),
    yearsOfExperience: z.number().int().min(0).max(70).optional(),
    consultationFee: z.number().min(0).max(100000).optional(),
  })
  .strict() // unknown fields are rejected
  .superRefine((data, ctx) => {
    if (data.role === 'doctor') {
      if (!data.specialization) {
        ctx.addIssue({ code: 'custom', path: ['specialization'], message: 'Doctors must give a specialization' });
      }
      if (!data.licenseNumber) {
        ctx.addIssue({ code: 'custom', path: ['licenseNumber'], message: 'Doctors must give a license number' });
      }
    }
  });

export const loginSchema = z
  .object({ email, password: z.string().min(1).max(200) })
  .strict();

export const mfaVerifySchema = z
  .object({ mfaToken: z.string().min(10).max(2000), code })
  .strict();

export const mfaEnableSchema = z.object({ code }).strict();

export { password as passwordSchema, email as emailSchema };
