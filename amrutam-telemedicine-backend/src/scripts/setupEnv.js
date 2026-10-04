// Creates a .env file with random secrets.  Run:  npm run setup:env
import fs from 'node:fs';
import crypto from 'node:crypto';

if (fs.existsSync('.env')) {
  console.log('.env already exists. I did not change it.');
  process.exit(0);
}

let text = fs.readFileSync('.env.example', 'utf8');
text = text
  .replace(/^JWT_SECRET=.*$/m, `JWT_SECRET=${crypto.randomBytes(48).toString('base64url')}`)
  .replace(/^ENCRYPTION_KEYS=.*$/m, `ENCRYPTION_KEYS=v1:${crypto.randomBytes(32).toString('base64')}`);

fs.writeFileSync('.env', text);
console.log('.env created with new random secrets. Never commit this file.');
