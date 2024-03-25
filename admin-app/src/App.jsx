import LoginScreen from '../pages/LoginScreen/LoginScreen.jsx'
import RegistrationScreen from '../pages/RegistrationScreen/RegistrationScreen.jsx'
import { Route, Routes } from 'react-router-dom'

export default function App() {
    return (
        <>
            <Routes>
                <Route path='/' element={ <RegistrationScreen /> } />
            </Routes>
        </>
    )
}