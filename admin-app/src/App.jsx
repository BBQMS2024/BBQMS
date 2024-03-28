import React from 'react';
import { Route, Routes } from 'react-router-dom';
import AuthGuard from './components/AuthGuard/AuthGuard.jsx';
import LoginScreen from './pages/LoginScreen/LoginScreen';
import RegistrationScreen from './pages/RegistrationScreen/RegistrationScreen';
import CompanyInfoUpdate from './pages/CompanyInfoUpdate/CompanyInfoUpdate';

export default function App() {
    return (
        <>
            <Routes>
                <Route path="/" element={ <LoginScreen /> } />
                <Route path="/registration" element={ <RegistrationScreen /> } />
                <Route path="/login" element={ <LoginScreen /> } />
                <Route path="/companydetails"
                       element={
                           <AuthGuard roles={ ['ROLE_SUPER_ADMIN'] }>
                               <CompanyInfoUpdate />
                           </AuthGuard>
                       }
                />
            </Routes>
        </>
    );
}
