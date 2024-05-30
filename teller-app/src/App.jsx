import React from 'react';
import { Route, Routes } from 'react-router-dom';
import StationIntroPage from './pages/StationIntroPage/StationIntroPage.jsx';
import ShowQueuesForTellerPage from './pages/ShowQueuesForTellerPage/ShowQueuesForTellerPage';
import Header from './components/Header/Header.jsx';
import CurrentTicketPage from './pages/CurrentTicketPage/CurrentTicketPage.jsx';

export default function App() {

    return (
        <>
            <Header />
            <Routes>
                <Route exact path='/' element={<StationIntroPage />} />
                <Route exact path="/teller-queue/:stationId" element={<ShowQueuesForTellerPage />} />
                <Route path="/display/:stationId" element={<CurrentTicketPage/>} />
            </Routes>
        </>
    );
}
