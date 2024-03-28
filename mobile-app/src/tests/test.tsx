import React from 'react';
import { render, fireEvent } from '@testing-library/react-native';
import WelcomeScreen from '../screens/WelcomeScreen';

test('valid code navigation', () => {
  const navigationMock = { navigate: jest.fn() };
  const { getByPlaceholderText, getByText } = render(<WelcomeScreen navigation={navigationMock} />);

  const codeInput = getByPlaceholderText('Enter code');
  fireEvent.changeText(codeInput, '1234');

  const submitButton = getByText('Submit');
  fireEvent.press(submitButton);

  expect(navigationMock.navigate).toHaveBeenCalledWith('Welcome');
});

test('invalid code alert', () => {
  const { getByPlaceholderText, getByText } = render(<WelcomeScreen route={undefined} />);

  const codeInput = getByPlaceholderText('Enter code');
  fireEvent.changeText(codeInput, '5678');

  const submitButton = getByText('Submit');
  fireEvent.press(submitButton);

  const alertMessage = 'Please enter a valid code.';
  expect(alertMessage).toBeTruthy();
});
