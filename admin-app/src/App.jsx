import React, { useEffect } from 'react';
import { Route, Routes, useNavigate } from 'react-router-dom';
import LoginScreen from './pages/LoginScreen/LoginScreen';
import RegistrationScreen from './pages/RegistrationScreen/RegistrationScreen';
import CompanyInfoUpdate from './pages/CompanyInfoUpdate/CompanyInfoUpdate';

export default function App() {
    const navigate = useNavigate();

    useEffect(() => {
        const userData = localStorage.getItem('userData');

        if (userData) {
            console.log(userData);
            navigate('/companydetails', { replace: false});
        }
    }, [navigate]);

    return (
        <>
            <Routes>
                <Route path='/' element={<LoginScreen />} />
                <Route path='/registration' element={<RegistrationScreen />} />
                <Route path='/login' element={<LoginScreen />} />
                <Route path='/companydetails' element={<CompanyInfoUpdate />} />
            </Routes>
        </>
    );
}
