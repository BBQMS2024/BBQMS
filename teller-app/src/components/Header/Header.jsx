import './Header.css';
import { useNavigate } from 'react-router-dom';


export default function Header() {
    const navigate = useNavigate();

    return (
        <header className="main-header">
            <h2 className="header-logo" onClick={() => navigate(`/`)}>BBQMS</h2>
        </header>
    );
}