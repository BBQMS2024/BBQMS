package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.exceptions.AuthException;
import ba.unsa.etf.si.bbqms.ws.models.UserDto;

import java.util.List;

public interface AdminService {
<<<<<<< HEAD
    List<User> findAdminsByCode(final String tenantCode);
    User addAdmin(final User admin, final String tenantCode) throws AuthException;

    User updateAdmin(final UserDto admin, final String tenantCode, final Long adminId) throws Exception;
    void removeAdmin(final String tenantCode, final Long adminId) throws Exception;
=======
    List<User> findUsersByCode(final String tenantCode, final String roleNames);
    User addUser(final User admin, final String tenantCode, final String roleName) throws AuthException;
    User updateUser(final UserDto request, final String tenantCode, final Long adminId);
    void removeUser(final String tenantCode, final Long adminId);
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
}
