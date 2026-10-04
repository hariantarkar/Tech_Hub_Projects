import { badRequest } from '../utils/errors.js';

// Usage: validate({ body: someSchema, query: otherSchema, params: anotherSchema })
// The clean (checked) data is placed in req.valid.body / req.valid.query / req.valid.params
export function validate(schemas) {
  return (req, res, next) => {
    const problems = [];
    req.valid = {};

    for (const part of ['params', 'query', 'body']) {
      if (!schemas[part]) continue;
      const result = schemas[part].safeParse(req[part] ?? {});
      if (result.success) {
        req.valid[part] = result.data;
      } else {
        for (const issue of result.error.issues) {
          problems.push({ where: part, field: issue.path.join('.'), message: issue.message });
        }
      }
    }

    if (problems.length > 0) {
      return next(badRequest('Some fields are not valid', 'VALIDATION_ERROR', problems));
    }
    next();
  };
}
