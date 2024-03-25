import React, { useState } from 'react';
import { Route, Routes, useNavigate } from "react-router-dom";
import * as yup from 'yup'
import "./RegistrationForm.css";
import RegistrationAuth from '../RegistrationAuth/RegistrationAuth';
import { SERVER_URL } from '../../src/constants';

const userSchema = yup.object().shape({
    email: yup.string().email().required(),
    password: yup.string().min(4).max(10).required()
})


export default function RegistrationForm(){

    const [isSubmitted, setIsSubmitted] = useState(false);
    const [qrCode, setQrCode] = useState("");
    const [email, setEamil] = useState("");
    const navigate = useNavigate();

    const createUser = async (event) =>{
        event.preventDefault();
        let formData = {
            email: event.target[0].value,
            password: event.target[1].value
        };

        const isValid = await userSchema.isValid(formData);
        if(isValid){
           setIsSubmitted(true);
           navigate('/');

           const userData = {
            email: formData.email,
            password: formData.password,
            phone_number: "00000"
           }

           const response = await fetch(SERVER_URL + '/api/v1/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
           })
           const body = await response.json();
           setEamil(body.email);
           
           try {
            const response = await fetch(SERVER_URL + `/api/v1/auth/tfa?email=${body.email}`);

            if (!response.ok) {
                throw new Error('Failed to fetch data');
            }

            const responseData = await response.json();

            setQrCode(responseData.qrCode);
            } catch (error) {
            console.error('Error fetching data:', error);
            }
        }
    }

    if (isSubmitted) {
        return (
            <Routes>
                <Route path='*' element={<RegistrationAuth qrCode={qrCode} email={email} />} />
            </Routes>
        )
    }

    return(
        <div id="login-form">
            <h1>Register Account</h1>
            <form onSubmit={createUser}>
                <label htmlFor="username">Email or phone number:</label>
                <input type="text" id="username" name="username" />
                <label htmlFor="password">Password:</label>
                <input type="password" id="password" name="password" />
                <input type="submit" value="Create new account"/>
            </form>
        </div>
    )
}