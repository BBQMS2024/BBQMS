import { GoogleLogin } from '@react-oauth/google';
import React, { useState } from "react";
import validator from "validator";
import "./LoginForm.css";
import LoginAuth from "../LoginAuth/LoginAuth";
import { Route, Routes, useNavigate, Link } from "react-router-dom";
import { SERVER_URL } from "../../constants.js";

const LoginForm = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [isSubmitted, setIsSubmitted] = useState(false);
    const navigate = useNavigate();

    async function handleGoogleLogin(credentialResponse) {
        const response = await fetch(`${SERVER_URL}/api/v1/auth/login/oauth2/google`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                googleToken: credentialResponse.credential
            })
        });
        if (!response.ok) {
            setError("Error while trying to log in with Google.");
            return;
        }
        const data = await response.json();
        localStorage.setItem('email', data.email);
        navigate('/');
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        console.log("username: "+ username);
        console.log("password: "+ password);

        if (!username.trim()) {
            setError("Username is required");
            return;
        }

        if (!validator.isEmail(username) && !validator.isMobilePhone(username, "any")) {
            setError("Invalid username format. Please enter a valid email or phone number.");
            return;
        }

        if (!password.trim()) {
            setError("Password is required.");
            return;
        }

        try {
            const response = await fetch(`${SERVER_URL}/api/v1/auth/login`,  {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: username,
                    password: password
                }),
            });

            const data = await response.json();

            localStorage.setItem('email', data.email);

            if (response.ok) {
                setIsSubmitted(true);
                navigate('/');
            } else if(response.status === 403){
                setError("Your credentials are incorrect.");
            }
        } catch (error) {
            console.error('Error:', error);
            setError("An error occurred. Please try again.");
        }
    };

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
        setError("");
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
        setError("");
    };

    if (isSubmitted) {
        return (
            <Routes>
                <Route path='/' element={<LoginAuth />} />
            </Routes>
        )
    }

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
                    {error && (error.includes("credentials")) && <p className="error">{error}</p>}
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
                    Not registered? <Link to="/registration">Create an account</Link>
                </p>
                <div style={{
                    display: 'flex',
                    justifyContent: 'center'
                }}>
                    <GoogleLogin onSuccess={ credentialResponse => handleGoogleLogin(credentialResponse)}
                    />
                </div>
            </form>
        </div>
    );
};

export default LoginForm;
