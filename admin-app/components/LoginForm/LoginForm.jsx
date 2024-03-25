import React from "react";
import "./LoginForm.css";

const LoginForm = () => {
  return (
    <div id="login-form">
      <h1>LOGIN</h1>
      <form>
        <label htmlFor="username">Email or phone number:</label>
        <input type="text" id="username" name="username" />
        <label htmlFor="password">Password:</label>
        <input type="password" id="password" name="password" />
        <input type="submit" value="Submit" />
      </form>
    </div>
  );
};

export default LoginForm;
