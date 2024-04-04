import { useContext } from 'react';
import { UserContext } from '../../context/UserContext.jsx';

export default function CanAccess( {children, roles} ){

    const {user, setUser} = useContext(UserContext);
    const hasRole = user && user.roles && user.roles.some(userRole => roles.find(role => role === userRole));
    return hasRole ? children : null;
}

