import CompanyInfoUpdate from './pages/CompanyInfoUpdate/CompanyInfoUpdate'
import { Route, Routes } from 'react-router-dom'

export default function App() {
    //CompanyInfoUpdate was loaded merely for testing purposes
    return (
        <>
            <Routes>
                <Route path='/' element={ <CompanyInfoUpdate /> } />
            </Routes>
        </>
    )
}

