import { describe, it, expect } from 'vitest';
import { encryptText, decryptText, needsReEncrypt } from '../../src/utils/crypto.js';

describe('field encryption', () => {
  it('encrypts and decrypts the same text', () => {
    const saved = encryptText('+919999999999');
    expect(saved).not.toContain('9999999999');
    expect(decryptText(saved)).toBe('+919999999999');
  });

  it('gives a different result each time (random iv)', () => {
    expect(encryptText('same')).not.toBe(encryptText('same'));
  });

  it('fails if the data was changed', () => {
    const parts = encryptText('secret').split(':');
    parts[3] = Buffer.from('tampered-data').toString('base64');
    expect(() => decryptText(parts.join(':'))).toThrow();
  });

  it('can still read data made with an old key (key rotation)', () => {
    const oldValue = encryptText('old data', 'v1'); // active key in tests is v2
    expect(needsReEncrypt(oldValue)).toBe(true);
    expect(decryptText(oldValue)).toBe('old data');
    expect(needsReEncrypt(encryptText('new data'))).toBe(false);
  });
});
