// One simple error class. The error handler turns it into a JSON response.
export class AppError extends Error {
  constructor(statusCode, code, message, details) {
    super(message);
    this.name = 'AppError';
    this.statusCode = statusCode;
    this.code = code;
    this.details = details;
  }
}

export const badRequest = (message, code = 'BAD_REQUEST', details) => new AppError(400, code, message, details);
export const unauthorized = (message = 'Please log in', code = 'UNAUTHORIZED') => new AppError(401, code, message);
export const forbidden = (message = 'You are not allowed to do this', code = 'FORBIDDEN') => new AppError(403, code, message);
export const notFound = (message = 'Not found', code = 'NOT_FOUND') => new AppError(404, code, message);
export const conflict = (message, code = 'CONFLICT') => new AppError(409, code, message);
