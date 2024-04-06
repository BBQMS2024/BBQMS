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
import ba.unsa.etf.si.bbqms.ws.models.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<User> findAdminsByCode(final String tenantCode, final List<String> roleNames){
        final Set<RoleName> roleNameSet = roleNames.stream()
                .map(RoleName::valueOf)
                .collect(Collectors.toSet());
        return this.userRepository.findAllByTenant_CodeAndRoles_NameIn(tenantCode, roleNameSet);
    }

    @Override
    public User addAdmin(final User admin, final String tenantCode, final String roleName) throws AuthException {
        if (!UserValidator.validateData(admin)) {
            throw new AuthException("Invalid email/password format");
        }

        final Optional<User> optionalExistingUser = this.userService.findByEmail(admin.getEmail());
        if (optionalExistingUser.isPresent()) {
            throw new AuthException("Tried making a user that already exists");
        }

        final Role role = this.roleService.getRoleByName(RoleName.valueOf(roleName))
                .orElseThrow(() -> new AuthException("Tried setting a role that doesn't exist"));
        final Tenant tenant = this.tenantService.findByCode(tenantCode);

        admin.setRoles(Set.of(role));
        admin.setTenant(tenant);
        admin.setOauth(false);
        admin.setTfaSecret(this.twoFactorService.generateNewSecret());
        admin.setPassword(this.passwordEncoder.encode(admin.getPassword().trim()));
        return this.userRepository.save(admin);
    }

    @Override
    public User updateAdmin(final UserDto request, final String tenantCode, final Long adminId) throws Exception {
        final User admin = this.userRepository.findById(adminId)
                .orElseThrow(() -> new NoSuchElementException("Admin user not found"));

        if (!admin.getTenant().getCode().equals(tenantCode)) {
            throw new IllegalArgumentException("Admin user does not belong to the specified tenant");
        }

        if (request.email() != null) {
            admin.setEmail(request.email());
        }

        if (request.phoneNumber() != null) {
            admin.setPhoneNumber(request.phoneNumber());
        }

        return this.userRepository.save(admin);
    }

    @Override
    public void removeAdmin(final String tenantCode, final Long adminId) throws Exception {
        final User admin = this.userRepository.findById(adminId)
                .orElseThrow(() -> new NoSuchElementException("Admin user not found"));

        if (!admin.getTenant().getCode().equals(tenantCode)) {
            throw new IllegalArgumentException("Admin user does not belong to the specified tenant");
        }
        this.userRepository.deleteById(adminId);
    }
}
