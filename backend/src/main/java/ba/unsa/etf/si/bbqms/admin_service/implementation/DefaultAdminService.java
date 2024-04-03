package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.AdminService;
import ba.unsa.etf.si.bbqms.auth_service.api.RoleService;
import ba.unsa.etf.si.bbqms.auth_service.api.UserService;
import ba.unsa.etf.si.bbqms.domain.Role;
import ba.unsa.etf.si.bbqms.domain.RoleName;
import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.exceptions.AuthException;
import ba.unsa.etf.si.bbqms.repository.UserRepository;
import ba.unsa.etf.si.bbqms.tenant_service.api.TenantService;
import ba.unsa.etf.si.bbqms.tfa_service.api.TwoFactorService;
import ba.unsa.etf.si.bbqms.utils.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DefaultAdminService implements AdminService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final TwoFactorService twoFactorService;
    private final TenantService tenantService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    public DefaultAdminService(final UserRepository userRepository,
                               final UserService userService,
                               final TwoFactorService twoFactorService,
                               final TenantService tenantService,
                               final RoleService roleService,
                               final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.twoFactorService = twoFactorService;
        this.tenantService = tenantService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAdminsByCode(final String tenantCode){
        final Set<RoleName> roleNameSet = Set.of(RoleName.ROLE_BRANCH_ADMIN);
        return userRepository.findAllByTenant_CodeAndRoles_NameIn(tenantCode, roleNameSet);
    }

    @Override
    public User addAdmin(final User admin, final String tenantCode) throws AuthException {
        if (!UserValidator.validateData(admin)) {
            throw new AuthException("Invalid email/password format.");
        }

        final Optional<User> optionalExistingUser = this.userService.findByEmail(admin.getEmail());
        if (optionalExistingUser.isPresent()) {
            throw new AuthException("Tried making a user that already exists.");
        }

        final Role role = this.roleService.getRoleByName(RoleName.ROLE_BRANCH_ADMIN)
                .orElseThrow(() -> new AuthException("Tried setting a role that doesn't exist."));//nek za sad svi budu super admin

        final Tenant tenant = this.tenantService.findByCode(tenantCode);

        admin.setRoles(Set.of(role));
        admin.setTenant(tenant);
        admin.setOauth(false);
        admin.setTfaSecret(this.twoFactorService.generateNewSecret());
        admin.setPassword(this.passwordEncoder.encode(admin.getPassword().trim()));
        return userRepository.save(admin);
    }
}
