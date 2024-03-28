import { fireEvent, render, waitFor } from "@testing-library/react-native";
import { Dialogs } from "../constants/dialogs";
import App from "../App";

const { validateCodeFormat } = require("../utils/validation");



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

test('testing welcoming message', async () => {
  const { getByPlaceholderText, getByText } = render(<App/>);
  const inputField = getByPlaceholderText(Dialogs.PROMPT.ENTER_CODE);

  console.log(inputField)
  // Simulate user typing into the input field
  fireEvent.changeText(inputField, 'DFLT');

  // Simulate button press
  fireEvent.press(getByText('Submit'));

  // Wait for fade-in animation to complete
  await waitFor(() => {
    // Assert that the welcome message is displayed
    expect(getByText('Welcome to BBQMS!')).toBeTruthy();
  });
});



