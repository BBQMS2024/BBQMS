import React, { useEffect, useState } from 'react';
import { Route, Routes } from 'react-router-dom';
import Header from './components/Header/Header.jsx';
import { SERVER_URL } from './constants.js';
import { UserContext } from './context/UserContext.jsx';
import { fetchData } from './fetching/Fetch.js';
import AuthGuard from './components/AuthGuard/AuthGuard';
import LoginScreen from './pages/LoginScreen/LoginScreen';
import CompanyInfoUpdate from './pages/CompanyInfoUpdate/CompanyInfoUpdate';
import HomePage from './pages/HomePage/HomePage';
import NotFound from './pages/NotFound/NotFound.jsx';
import CanAccess from './components/CanAccess/CanAccess';
import HomePageCard from './components/HomePageCard/HomePageCard';
import AdminProfile from './pages/AdminProfile/AdminProfile';
import LoginAuth from './components/LoginAuth/LoginAuth';
import ManageAdmins from './pages/AdminManagingScreen/AdminManagingScreen';
import ManageServices from './pages/ManageServices/ManageServices';
import ManageBranches from './pages/ManageBranchesScreen/ManageBranchesScreen';
import ManageGroups from './pages/ManageGroupsScreen/ManageGroupsScreen';
import ManageStations from './pages/ManageStationScreen/ManageStationScreen';
import ManageDisplays from './pages/ManageDisplays/ManageDisplays';
import ManageUsers from './pages/UserManagingScreen/UserManagingScreen';
import ViewQueues from './pages/ViewBranchQueues/ViewBranchQueues';
import { ROLES } from './constants.js';

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
                    <Route exact path="/:tenantCode/manage/displays" element={
                        <AuthGuard roles={ [ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN] }>
                            <ManageDisplays />
                        </AuthGuard> } />
                    <Route exact path="/:tenantCode/manage/stations" element={
                        <AuthGuard roles={ [ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN] }>
                            <ManageStations />
                        </AuthGuard> } />
                    <Route exact path="/:tenantCode/manage/groups" element={
                        <AuthGuard roles={ [ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN] }>
                            <ManageGroups />
                        </AuthGuard> } />

                    <Route exact path="/:tenantCode/manage/branches" element={
                        <AuthGuard roles={ [ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN] }>
                            <ManageBranches />
                        </AuthGuard> } />

                    <Route exact path="/:tenantCode/companydetails"
                           element={
                               <AuthGuard roles={ [ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN] }>
                                   <CompanyInfoUpdate />
                               </AuthGuard>
                           }
                    />
                    <Route exact path="/:tenantCode/manage/services" element={
                        <AuthGuard roles={ [ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN] }>
                            <ManageServices />
                        </AuthGuard> }
                    />
                    <Route exact path="/:tenantCode/manage/users" element={
                        <AuthGuard roles={[ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN]}>
                            <ManageUsers />
                        </AuthGuard>
                    } />
                    <Route exact path="/login" element={ <LoginScreen /> } />
                    <Route exact path="/:tenantCode/manage/admins" element={
                        <AuthGuard roles={ [ROLES.ROLE_SUPER_ADMIN] }>
                            <ManageAdmins />
                        </AuthGuard> } />
                    <Route exact path="/profile" element={
                        <AuthGuard roles={ [ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN] }>
                            <AdminProfile />
                        </AuthGuard> } />
                    <Route exact path="/" element={ <LoginScreen /> } />
                    <Route exact path="/loginauth"
                           element={
                               <AuthGuard roles={ [ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN] }>
                                   <LoginAuth />
                               </AuthGuard>
                           }
                    />
                    <Route exact path="/:tenantCode/queues" element={
                        <AuthGuard roles={ [ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN] }>
                            <ViewQueues />
                        </AuthGuard> } />

                    <Route exact path="/:tenantCode/home" element={
                        <AuthGuard roles={ [ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN] }>
                            <HomePage></HomePage>
                            <CanAccess roles={ [ROLES.ROLE_SUPER_ADMIN, ROLES.ROLE_BRANCH_ADMIN] }>
                                <HomePageCard title="Manage displays" backgroundColor="var(--dark-blue)"
                                              buttonColor="var(--light-blue)"
                                              url={ '/DFLT/manage/displays' }></HomePageCard>
                                <HomePageCard title="Manage groups" backgroundColor="var(--light-blue)"
                                              buttonColor="var(--dark-blue)"
                                              url={ '/DFLT/manage/groups' }></HomePageCard>
                                <HomePageCard title="Manage branches" backgroundColor="var(--dark-blue)"
                                              buttonColor="var(--light-blue)"
                                              url={ '/DFLT/manage/branches' }></HomePageCard>
                                <HomePageCard title="Manage services" backgroundColor="var(--light-blue)"
                                              buttonColor="var(--dark-blue)"
                                              url={ '/DFLT/manage/services' }></HomePageCard>
                                <HomePageCard title="Manage teller stations" backgroundColor="var(--light-blue)"
                                              buttonColor="var(--dark-blue)"
                                              url={ '/DFLT/manage/stations' }></HomePageCard>
                                <HomePageCard title="Manage company details" backgroundColor="var(--dark-blue)"
                                              buttonColor="var(--light-blue)"
                                              url={ '/DFLT/companydetails' }></HomePageCard>
                                <HomePageCard title="View queues" backgroundColor="var(--light-blue)"
                                              buttonColor="var(--dark-blue)"
                                              url={ '/DFLT/queues' }></HomePageCard>
                            </CanAccess>
                            <CanAccess roles={ [ROLES.ROLE_SUPER_ADMIN] }>
                                <HomePageCard title="Manage administrators" backgroundColor="var(--dark-blue)"
                                              buttonColor="var(--light-blue)"
                                              url={ '/DFLT/manage/admins' }></HomePageCard>
                                <HomePageCard
                                    title="Manage users"
                                    backgroundColor="var(--light-blue)"
                                    buttonColor="var(--dark-blue)"
                                    url={'/DFLT/manage/users'}
                                />
                            </CanAccess>

                            <CanAccess roles={[ROLES.ROLE_BRANCH_ADMIN]}>
                                <HomePageCard
                                    title="Manage users"
                                    backgroundColor="var(--dark-blue)"
                                    buttonColor="var(--light-blue)"
                                    url={'/DFLT/manage/users'}
                                />
                            </CanAccess>
                        </AuthGuard>
                    } />
                    <Route path="*" element={ <NotFound /> } />
                </Routes>
            </UserContext.Provider>
        </>
    );
}