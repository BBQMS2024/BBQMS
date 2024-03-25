import React, { useState } from 'react';
import { Route, Routes, useNavigate } from "react-router-dom";
import * as yup from 'yup'
import "./RegistrationForm.css";
import RegistrationAuth from '../RegistrationAuth/RegistrationAuth';

const userSchema = yup.object().shape({
    email: yup.string().email().required(),
    password: yup.string().min(4).max(10).required()
})


export default function RegistrationForm(){

    const [isSubmitted, setIsSubmitted] = useState(false);
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
        }
    }

    if (isSubmitted) {
        return (
            <Routes>
                <Route path='/' element={<RegistrationAuth />} />
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