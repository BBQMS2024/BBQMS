import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import logoImage from '../../../assets/logo-image.jpg';
import profileImage from '../../../assets/profile-user.png'

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
            <h2 className="header-logo" onClick={() => navigate('/')}>BBQMS</h2>

            <div className="header-logout">
                {!!user && (
                    <button className="header-logout-btn" onClick={handleLogout}>
                        Logout
                    </button>
                )}
            </div>

            <div className="header-profile" onClick={() => navigate('/')}>
                <img src={profileImage} className="header-profile-png" alt="Profile image"/>
            </div>


        </header>
    );
}