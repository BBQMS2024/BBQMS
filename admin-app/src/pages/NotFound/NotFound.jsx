import { useNavigate } from 'react-router-dom';
import { UserContext } from '../../context/UserContext.jsx';
import {useContext} from "react";
export default function NotFound() {
    const navigate = useNavigate();
    const { user, setUser } = useContext(UserContext);

    return (
        <div style={ {
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            height: '100%',
            gap: '10px'
        } }>
            <p>
                404 - The page does not exist.
            </p>
            <button style={ {
                width: 'fit-content',
                border: '1px solid blue',
                borderRadius: '5px',
                padding: '5px 30px',
                cursor: 'pointer'
            } }
                    onClick={ () => navigate((`${user.tenantCode}/home`)) }>
                Go back home?
            </button>
        </div>
    );
}