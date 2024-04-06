import React from 'react';
import { Route, Routes} from 'react-router-dom';
import AuthGuard from './components/AuthGuard/AuthGuard';
import LoginScreen from './pages/LoginScreen/LoginScreen';
import CompanyInfoUpdate from './pages/CompanyInfoUpdate/CompanyInfoUpdate';
import LoginAuth from './components/LoginAuth/LoginAuth';

export default function App() {
    return (
        <>
            <Routes>
                <Route path="/" element={ <LoginScreen /> } />
                <Route path="/login" element={ <LoginScreen /> } />
                {/*<Route path="/loginAuth"
                        element={
                            <AuthGuard roles={ ['ROLE_SUPER_ADMIN'] }>
                                <LoginAuth />
                            </AuthGuard>
                        } />*/}
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
