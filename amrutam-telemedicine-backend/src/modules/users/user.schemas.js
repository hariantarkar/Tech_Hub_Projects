import { z } from 'zod';

const dateOnly = z
  .string()
  .regex(/^\d{4}-\d{2}-\d{2}$/, 'Use the format YYYY-MM-DD')
  .refine((value) => {
    const date = new Date(`${value}T00:00:00Z`);
    return !Number.isNaN(date.getTime()) && date.toISOString().startsWith(value) && date <= new Date();
  }, 'Date is not valid or is in the future');

export const updateProfileSchema = z
  .object({
    fullName: z.string().trim().min(2).max(100).optional(),
    phone: z.string().trim().regex(/^\+?[0-9]{10,15}$/, 'Phone must have 10 to 15 digits').optional(),
    dateOfBirth: dateOnly.optional(),
    gender: z.enum(['male', 'female', 'other']).optional(),
  })
  .strict()
  .refine((data) => Object.keys(data).length > 0, 'Send at least one field to update');
