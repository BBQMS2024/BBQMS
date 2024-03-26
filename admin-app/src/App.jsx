import { Route, Routes } from 'react-router-dom'
import RegistrationScreen from './pages/RegistrationScreen/RegistrationScreen'

export default function App() {
    return (
        <>
            <Routes>
                <Route path='/' element={ <RegistrationScreen /> } />
            </Routes>
        </>
    )
}