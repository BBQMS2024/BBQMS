import { useContext } from 'react';
import { Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { lastPathPart } from '../../utils/StringUtils.js';
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

    const isHomePage = 'home' === lastPathPart(window.location.href)

    return (
        <>
            <header className="main-header">
                <h2 className="header-logo" onClick={ () => navigate(`${ user.tenantCode }/home`) }>BBQMS</h2>

                <div className="header-logout">
                    { !!user && (
                        <button className="header-logout-btn" onClick={ handleLogout }>
                            Logout
                        </button>
                    ) }
                </div>

                <div className="header-profile" onClick={ () => navigate('/profile') }>
                    <img src={ profileImage } className="header-profile-png" alt="Profile image" />
                </div>
            </header>
            { !isHomePage && (
                <Button variant="secondary"
                        className="mt-2 px-4"
                        onClick={ () => navigate(`${ user.tenantCode }/home`) }>
                    Back
                </Button>
            ) }
        </>

    );
}