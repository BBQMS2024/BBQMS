import React, { useEffect, useState } from 'react';
import { Route, Routes } from 'react-router-dom';
import Header from './components/Header/Header.jsx';
import { SERVER_URL } from './constants.js';
import { UserContext } from './context/UserContext.jsx';
import { fetchData } from './fetching/Fetch.js';
import AuthGuard from './components/AuthGuard/AuthGuard';
import LoginScreen from './pages/LoginScreen/LoginScreen';
import CompanyInfoUpdate from './pages/CompanyInfoUpdate/CompanyInfoUpdate';
import HomePage from './pages/HomePage/HomePage'
import NotFound from './pages/NotFound/NotFound.jsx';
import CanAccess from './components/CanAccess/CanAccess';
import HomePageCard from './components/HomePageCard/HomePageCard';
import 'bootstrap/dist/css/bootstrap.min.css';


export default function App() {
    const [user, setUser] = useState();

    /*
        Kada se logiramo, ako vec postoji token u localStorage, provjerimo da li je validan (nije istekao)
        Ako je validan, ulogujemo usera, ako nije ocistimo storage od starih podataka
     */
    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            const url = `${ SERVER_URL }/api/v1/auth`;
            fetchData(url, 'GET')
                .then(({ data, success }) => {
                    if (success) {
                        setUser(JSON.parse(localStorage.getItem('userData')));
                    } else {
                        localStorage.removeItem('token');
                        localStorage.removeItem('userData');
                    }
                });
        }
    }, []);

    return (
        <>
            <UserContext.Provider value={ { user, setUser } }>
                <Header />
                <Routes>
                    <Route exact path="/:tenantCode/companydetails"
                           element={
                               <AuthGuard roles={ ['ROLE_SUPER_ADMIN'] }>
                                   <CompanyInfoUpdate />
                               </AuthGuard>
                           }
                    />
                    <Route exact path="/login" element={ <LoginScreen /> } />
                    <Route exact path="/" element={ <LoginScreen /> } />
                    <Route exact path = "/:tenantCode/home" element = {
                        <AuthGuard roles={ ['ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN'] }>
                        <HomePage></HomePage>
                        <CanAccess roles={ ['ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN'] }>
                            <HomePageCard title = "Manage groups" backgroundColor = "#476072" buttonColor = "#548CA8"></HomePageCard>
                            <HomePageCard title = "Manage displays" backgroundColor = "#548CA8" buttonColor = "#476072"></HomePageCard>
                            <HomePageCard title = "Manage branches" backgroundColor = "#476072" buttonColor = "#548CA8"></HomePageCard>
                            <HomePageCard title = "Manage services offered by a company" backgroundColor = "#548CA8" buttonColor = "#476072"></HomePageCard>
                            <HomePageCard title = "Manage teller stations" backgroundColor = "#476072" buttonColor = "#548CA8"></HomePageCard>
                            <HomePageCard title = "Manage company details" backgroundColor = "#476072" buttonColor = "#548CA8" url = "/:tenantCode/companydetails"></HomePageCard>
                        </CanAccess>
                            <CanAccess roles={ ['ROLE_SUPER_ADMIN'] }>
                                <HomePageCard title = "Manage administrators" backgroundColor = "#548CA8" buttonColor = "#476072"></HomePageCard>
                            </CanAccess>
            </AuthGuard>
                    } />
                    <Route path="*" element={ <NotFound /> } />
                </Routes>
            </UserContext.Provider>
        </>

    )
}
