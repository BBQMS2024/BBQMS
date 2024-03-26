import LoginScreen from './pages/LoginScreen/LoginScreen'
import RegistrationScreen from './pages/RegistrationScreen/RegistrationScreen'
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