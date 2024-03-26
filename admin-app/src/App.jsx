import LoginScreen from './pages/LoginScreen/LoginScreen'
import { Route, Routes } from 'react-router-dom'

export default function App() {
    return (
        <>
            <Routes>
                <Route path='/' element={ <LoginScreen /> } />
            </Routes>
        </>
    )
}