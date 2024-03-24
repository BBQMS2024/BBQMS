package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.AdminService;
import org.springframework.stereotype.Service;

@Service
public class DefaultAdminService implements AdminService {
    public String dummyAdminServiceImpl() {
        return "Hello admin";
    }
}
