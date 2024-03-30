import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import logoImage from '../../../assets/bbqms-logo.png';

import './Header.css';
import { UserContext } from '../../context/UserContext.jsx';

export default function Header() {
    const navigate = useNavigate();

    const { user, setUser } = useContext(UserContext);

    function handleLogout() {
        localStorage.removeItem('userData');
        localStorage.removeItem('token');
        setUser(null);
        navigate('/');
    }

    return (
        <header className="main-header">

            <div className="header-logo" onClick={ () => navigate('/') }>
                <h1>BBQMS</h1>
                <img src={ logoImage } className="header-logo-png" alt="BBQMS logo" />
            </div>
            <div className="header-logout">
                { !!user && (
                    <button className="header-logout-btn" onClick={ handleLogout }>
                        Logout
                    </button>
                ) }

            </div>
        </header>
    );
}