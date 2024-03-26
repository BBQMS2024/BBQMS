package ba.unsa.etf.si.bbqms.auth_service.api;

import ba.unsa.etf.si.bbqms.domain.User;

import java.util.Optional;

public interface OAuthService {
    Optional<User> authenticateUser(final String googleCredentials);
}
