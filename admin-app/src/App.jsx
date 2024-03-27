import LoginScreen from './pages/LoginScreen/LoginScreen';
import { Route, Routes } from 'react-router-dom';
import RegistrationScreen from './pages/RegistrationScreen/RegistrationScreen.jsx';
import CompanyDetailsForm from "./components/CompanyDetailsForm/CompanyDetailsForm.jsx";

export default function App() {
    return (
        <>
            <Routes>
                <Route path='/' element={<LoginScreen />} />
                <Route path='/registration' element={<RegistrationScreen />} />
                <Route path='/login' element={<LoginScreen />} />
                <Route path='/companydetails' element={<CompanyDetailsForm />} />
            </Routes>
        </>
    );
}
