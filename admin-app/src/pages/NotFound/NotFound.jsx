import { useNavigate } from 'react-router-dom';

export default function NotFound() {
    const navigate = useNavigate();

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
                    onClick={ () => navigate(('/')) }>
                Go back home?
            </button>
        </div>
    );
}