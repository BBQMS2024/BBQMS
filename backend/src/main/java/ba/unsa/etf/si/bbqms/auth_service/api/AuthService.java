package ba.unsa.etf.si.bbqms.auth_service.api;

import ba.unsa.etf.si.bbqms.domain.RoleName;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.exceptions.AuthException;

import java.util.Optional;

public interface AuthService {
    User registerUser(final User user) throws AuthException;
    Optional<User> loginUser(final User user);
    Optional<User> findByEmail(final String email);
    String generateUserToken(final User user);
    Optional<User> getCurrentUser();
    String generateUserQrCode(final User user);
    boolean verifyUserTfaCode(final User user, final String code);
    boolean setTfaUse(final boolean tfa) throws Exception;
    User getAuthenticatedUser();
    boolean canChangeTenant(final String tenantCode);
    boolean canOnlyCRUDUser(final RoleName roleName);
}
