const { validateCodeFormat } = require("../utils/validation");
const { Dialogs } = require("../constants/dialogs");



describe('Code validation tests', () => {

  test('Valid and invalid code formats', () => {
    // Valid code formats
    expect(validateCodeFormat('ABCD')).toBe(true); 
    expect(validateCodeFormat('WXYZ')).toBe(true); 
    expect(validateCodeFormat('1234')).toBe(true); 
    // Invalid code formats
    expect(validateCodeFormat('ABCD1')).toBe(false); 
    expect(validateCodeFormat('ABCD!')).toBe(false); 
    expect(validateCodeFormat('ABC')).toBe(false); 
  });
});





