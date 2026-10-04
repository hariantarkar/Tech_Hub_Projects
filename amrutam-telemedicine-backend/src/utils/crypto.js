import crypto from 'node:crypto';
import { env } from '../config/env.js';

// Field encryption: AES-256-GCM.
// Saved text looks like:  keyId:iv:authTag:cipherText   (all base64 except keyId)
// Because the keyId is saved with the data, we can rotate keys:
//   1. add a new key to ENCRYPTION_KEYS
//   2. set ENCRYPTION_ACTIVE_KEY_ID to the new id
//   3. old data is still readable, new data uses the new key

function loadKeys() {
  const keys = new Map();
  for (const part of env.ENCRYPTION_KEYS.split(',')) {
    const [id, base64Key] = part.trim().split(':');
    if (!id || !base64Key) {
      throw new Error('ENCRYPTION_KEYS must look like  v1:base64key,v2:base64key');
    }
    const key = Buffer.from(base64Key, 'base64');
    if (key.length !== 32) {
      throw new Error(`Encryption key "${id}" must be 32 bytes (base64 of 32 random bytes)`);
    }
    keys.set(id, key);
  }
  if (!keys.has(env.ENCRYPTION_ACTIVE_KEY_ID)) {
    throw new Error(`ENCRYPTION_ACTIVE_KEY_ID "${env.ENCRYPTION_ACTIVE_KEY_ID}" is not in ENCRYPTION_KEYS`);
  }
  return keys;
}

const keys = loadKeys();

export function encryptText(plainText, keyId = env.ENCRYPTION_ACTIVE_KEY_ID) {
  const key = keys.get(keyId);
  if (!key) throw new Error(`Unknown encryption key id: ${keyId}`);

  const iv = crypto.randomBytes(12);
  const cipher = crypto.createCipheriv('aes-256-gcm', key, iv);
  const encrypted = Buffer.concat([cipher.update(String(plainText), 'utf8'), cipher.final()]);
  const authTag = cipher.getAuthTag();

  return [keyId, iv.toString('base64'), authTag.toString('base64'), encrypted.toString('base64')].join(':');
}

export function decryptText(savedText) {
  const [keyId, ivText, tagText, dataText] = String(savedText).split(':');
  const key = keys.get(keyId);
  if (!key || !ivText || !tagText || !dataText) throw new Error('Cannot decrypt: bad format or unknown key');

  const decipher = crypto.createDecipheriv('aes-256-gcm', key, Buffer.from(ivText, 'base64'));
  decipher.setAuthTag(Buffer.from(tagText, 'base64'));
  const plain = Buffer.concat([decipher.update(Buffer.from(dataText, 'base64')), decipher.final()]);
  return plain.toString('utf8');
}

// True when the value was made with an old key (a background job can re-encrypt it).
export function needsReEncrypt(savedText) {
  return String(savedText).split(':')[0] !== env.ENCRYPTION_ACTIVE_KEY_ID;
}

export function sha256(text) {
  return crypto.createHash('sha256').update(text).digest('hex');
}
