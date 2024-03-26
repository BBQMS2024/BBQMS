package ba.unsa.etf.si.bbqms.auth_service.api;

import ba.unsa.etf.si.bbqms.domain.Role;
import ba.unsa.etf.si.bbqms.domain.RoleName;

import javax.naming.AuthenticationException;
import java.util.Optional;
import java.util.Set;

public interface RoleService {
    Optional<Role> getRoleByName(final RoleName roleName);
}
