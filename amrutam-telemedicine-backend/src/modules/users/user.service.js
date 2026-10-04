import { encryptText, decryptText } from '../../utils/crypto.js';
import { notFound } from '../../utils/errors.js';

export function createUserService({ db, auditService }) {
  async function getMe(userId) {
    const result = await db.query(
      `SELECT u.id, u.email, u.role, u.status, u.mfa_enabled, u.created_at,
              p.full_name, p.phone_encrypted, to_char(p.date_of_birth, 'YYYY-MM-DD') AS date_of_birth, p.gender,
              d.specialization, d.license_number, d.years_of_experience, d.consultation_fee, d.is_verified
       FROM users u
       LEFT JOIN profiles p ON p.user_id = u.id
       LEFT JOIN doctors d ON d.user_id = u.id
       WHERE u.id = $1`,
      [userId],
    );
    const row = result.rows[0];
    if (!row) throw notFound('User not found');

    const me = {
      id: row.id,
      email: row.email,
      role: row.role,
      status: row.status,
      mfaEnabled: row.mfa_enabled,
      createdAt: row.created_at,
      fullName: row.full_name,
      phone: row.phone_encrypted ? decryptText(row.phone_encrypted) : null,
      dateOfBirth: row.date_of_birth,
      gender: row.gender,
    };

    if (row.role === 'doctor') {
      me.doctor = {
        specialization: row.specialization,
        licenseNumber: row.license_number,
        yearsOfExperience: row.years_of_experience,
        consultationFee: Number(row.consultation_fee),
        isVerified: row.is_verified,
      };
    }
    return me;
  }

  async function updateMe(userId, input, ctx) {
    // Only these fields can be changed (allow list).
    const columnFor = {
      fullName: ['full_name', (v) => v],
      phone: ['phone_encrypted', encryptText],
      dateOfBirth: ['date_of_birth', (v) => v],
      gender: ['gender', (v) => v],
    };

    const setParts = [];
    const values = [userId];
    for (const [field, [column, convert]] of Object.entries(columnFor)) {
      if (input[field] === undefined) continue;
      values.push(convert(input[field]));
      setParts.push(`${column} = $${values.length}`);
    }

    const result = await db.query(
      `UPDATE profiles SET ${setParts.join(', ')}, updated_at = now() WHERE user_id = $1`,
      values,
    );
    if (result.rowCount === 0) throw notFound('Profile not found');

    await auditService.recordSafe({
      actorId: userId,
      actorRole: ctx.role,
      action: 'profile.update',
      entityType: 'user',
      entityId: userId,
      ip: ctx.ip,
      requestId: ctx.requestId,
      details: { fields: Object.keys(input) }, // field names only, never the values
    });

    return getMe(userId);
  }

  return { getMe, updateMe };
}
