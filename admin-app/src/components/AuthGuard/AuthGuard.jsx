import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function AuthGuard({ children, roles }) {
    const navigate = useNavigate();

    useEffect(() => {
        const storedUserData = localStorage.getItem('userData');
        const user = storedUserData ? JSON.parse(storedUserData) : null;

        if (!user) {
            navigate('/login');
            return;
        }

        const hasNecessaryRole = roles.some(role => user.roles.find(userRole => userRole === role));

        if (!hasNecessaryRole) {
            navigate('/');
            return;
        }
    }, []);

    return (
        <>
            { children }
        </>
    );

}