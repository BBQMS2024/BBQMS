import LoginScreen from './pages/LoginScreen/LoginScreen';
import { Route, Routes } from 'react-router-dom';
import RegistrationScreen from './pages/RegistrationScreen/RegistrationScreen.jsx';

export default function App() {
    return (
        <>
            <Routes>
                <Route path='/' element={<LoginScreen />} />
                <Route path='/registration' element={<RegistrationScreen />} />
                <Route path='/login' element={<LoginScreen />} />
            </Routes>
        </>
    );
}
