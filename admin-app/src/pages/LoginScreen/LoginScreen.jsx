import { GoogleLogin } from '@react-oauth/google';
import React, { useContext, useState } from 'react';
import validator from 'validator';
import './LoginScreen.css';
import { useNavigate } from 'react-router-dom';
import { SERVER_URL } from '../../constants.js';
import { UserContext } from '../../context/UserContext.jsx';
import { fetchData } from '../../fetching/Fetch.js';

export default function LoginScreen() {
    const navigate = useNavigate();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isSubmitted, setIsSubmitted] = useState(false);

    const { user, setUser } = useContext(UserContext);

    // Nemojte za sad koristit google login, to cemo pravit kasnije ak budemo morali
    // nek stoji samo
    async function handleGoogleLogin(credentialResponse) {
        const url = `${ SERVER_URL }/api/v1/auth/login/oauth2/google`;
        const { data, success } = await fetchData(url, 'POST', {
            googleToken: credentialResponse.credential
        })

        if (!success) {
            setError('Error while trying to log in with Google.');
            return;
        }
        localStorage.setItem('userData', JSON.stringify(data));
        navigate('/home');
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!username.trim()) {
            setError('Username is required');
            return;
        }

        if (!validator.isEmail(username) && !validator.isMobilePhone(username, 'any')) {
            setError('Invalid username format. Please enter a valid email or phone number.');
            return;
        }

        if (!password.trim()) {
            setError('Password is required.');
            return;
        }

        try {
            const url = `${ SERVER_URL }/api/v1/auth/login`;
            const { data, success } = await fetchData(url, 'POST', {
                email: username,
                password: password
            });

            if (success) {
                if(data.userData == undefined){
                    localStorage.setItem('userData', JSON.stringify(data));
                }else{
                    localStorage.setItem('userData', JSON.stringify(data.userData));
                    localStorage.setItem('token', data.token);
                    setUser(data.userData);
                }
                setIsSubmitted(true);

                if(data.token){
                    localStorage.setItem('isTfa', false);
                    navigate(`/${ data.userData.tenantCode }/home`);
                }else{
                    localStorage.setItem('isTfa', true);
                    navigate('/loginauth');
                }
            } else {
                setError('Your credentials are incorrect.');
            }
        } catch (error) {
            console.error('Error:', error);
            setError('An error occurred. Please try again.');
        }
    };

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
        setError('');
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
        setError('');
    };
    /*
        if (isSubmitted) {
            //navigate('/loginAuth');
            navigate('/companydetails');
        }*/

    return (
        <div id="login-form">
            <h1>LOGIN</h1>
            <form onSubmit={ handleSubmit }>
                <div className="form-group">
                    <label htmlFor="username">Email or phone number:</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={ username }
                        onChange={ handleUsernameChange }
                    />
                    { error && (error.includes('Username') || error.includes('Invalid')) &&
                        <p className="error">{ error }</p> }
                    { error && (error.includes('credentials')) && <p className="error">{ error }</p> }
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={ password }
                        onChange={ handlePasswordChange }
                    />
                    { error && error.includes('Password') && <p className="error">{ error }</p> }
                </div>
                <input type="submit" value="Submit" />
                <div style={ {
                    display: 'flex',
                    justifyContent: 'center'
                } }>
                    <GoogleLogin onSuccess={ credentialResponse => handleGoogleLogin(credentialResponse) }
                    />
                </div>
            </form>
        </div>
    );
};