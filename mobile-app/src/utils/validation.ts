function validateCodeFormat(code: string): boolean {
    return /^[A-Z0-9]{4}$/.test(code);
}

export { validateCodeFormat };