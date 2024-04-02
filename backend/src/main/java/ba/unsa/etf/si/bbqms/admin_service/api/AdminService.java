package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.exceptions.AuthException;

import java.util.List;

public interface AdminService {
    List<User> findAdminsByCode(final String tenantCode);
    User addAdmin(final User admin, final String tenantCode) throws AuthException;
}
