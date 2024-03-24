import React, { useState } from "react";
import validator from "validator";
import "./LoginForm.css";

const LoginForm = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = (event) => {
        event.preventDefault();

        if (!username.trim()) {
            setError("Username is required");
            return;
        }

        if (!validator.isEmail(username) && !validator.isMobilePhone(username, "any")) {
            setError("Invalid username format. Please enter a valid email or phone number.");
            return;
        }

        if (!password.trim()) {
            setError("Password is required");
            return;
        }

        setUsername("");
        setPassword("");
        setError("");
    };

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
        setError("");
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
        setError("");
    };

    return (
        <div id="login-form">
            <h1>LOGIN</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Email or phone number:</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={username}
                        onChange={handleUsernameChange}
                    />
                    {error && (error.includes("Username") || error.includes("Invalid")) && <p className="error">{error}</p>}
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={password}
                        onChange={handlePasswordChange}
                    />
                    {error && error.includes("Password") && <p className="error">{error}</p>}
                </div>
                <input type="submit" value="Submit" />
                <p id="registration">
                    Not registered? <a href="https://c2.etf.unsa.ba/">Create an account</a>
                </p>
            </form>
        </div>
    );
};

export default LoginForm;