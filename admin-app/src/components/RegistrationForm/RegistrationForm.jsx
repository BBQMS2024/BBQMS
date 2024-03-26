import React, { useState } from 'react';
import { Route, Routes, useNavigate } from "react-router-dom";
import { useFormik } from 'formik';
import * as yup from 'yup';
import "./RegistrationForm.css";
import RegistrationAuth from '../RegistrationAuth/RegistrationAuth';
import { SERVER_URL } from '../../constants';

const userSchema = yup.object().shape({
    email: yup.string().email("Please enter a valid email").required("Email is required"),
    password: yup.string().min(4).max(10).required("Password is required")
});

export default function RegistrationForm() {
    const navigate = useNavigate();
    const [nextPage, setNextPage] = useState(false);
    const [qrCode, setQrCode] = useState("");
    const [email, setEmail] = useState("");

    const formik = useFormik({
        initialValues: {
            email: '',
            password: ''
        },
        validationSchema: userSchema,
        onSubmit: async (values, { setFieldError }) => {
            try {
                const isValid = await userSchema.isValid(values);
                if (isValid) {
                    const response = await fetch(SERVER_URL + '/api/v1/auth/register', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            email: values.email,
                            password: values.password,
                            phone_number: "00000"
                        })
                    });

                    if (response.ok) {
                        const body = await response.json();
                        const responseBody = await fetch(SERVER_URL + `/api/v1/auth/tfa?email=${body.email}`);
                        const responseData = await responseBody.json();
                        setQrCode(responseData.qrCode);
                        setEmail(body.email);
                        setNextPage(true);
                    }else if (response.status === 400) {
                        setFieldError('email', 'Email already in use');
                    } else {
                        console.error('Failed to register:', response.statusText);
                    }
                }
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        }
    });

    if (nextPage) {
        return (
            <Routes>
                <Route path='*' element={<RegistrationAuth qrCode={qrCode} email={email} />} />
            </Routes>
        );
    }

    return (
        <div id="login-form">
            <h1>Register Account</h1>
            <form onSubmit={formik.handleSubmit}>
                <label htmlFor="email">Email or phone number:</label>
                <input
                    id="email"
                    name="email"
                    type="text"
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    value={formik.values.email}
                />
                {formik.touched.email && formik.errors.email ? (
                    <p className="error">{formik.errors.email}</p>
                ) : null}
                <label htmlFor="password">Password:</label>
                <input
                    id="password"
                    name="password"
                    type="password"
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    value={formik.values.password}
                />
                {formik.touched.password && formik.errors.password ? (
                    <p className="error">{formik.errors.password}</p>
                ) : null}
                <input type="submit" value="Create new account"/>
            </form>
        </div>
    );
}
