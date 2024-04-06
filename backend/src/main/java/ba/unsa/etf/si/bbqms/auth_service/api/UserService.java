package ba.unsa.etf.si.bbqms.auth_service.api;

import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.exceptions.AuthException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User save(final User user);
    Optional<User> findByEmail(final String email);
    boolean setUserTfa(final boolean tfa) throws AuthException;
}
