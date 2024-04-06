package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.AdminService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
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
    private final AuthService authService;
    public DefaultAdminService(final UserRepository userRepository,
                               final UserService userService,
                               final TwoFactorService twoFactorService,
                               final TenantService tenantService,
                               final RoleService roleService,
                               final PasswordEncoder passwordEncoder,
                               final AuthService authService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.twoFactorService = twoFactorService;
        this.tenantService = tenantService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @Override
    public List<User> findUsersByCode(final String tenantCode, final String roleName){
        final Set<RoleName> roleNameSet = Set.of(RoleName.valueOf(roleName));
        return this.userRepository.findAllByTenant_CodeAndRoles_NameIn(tenantCode, roleNameSet);
    }

    @Override
    public User addUser(final User user, final String tenantCode, final String roleName) throws AuthException {
        if (!UserValidator.validateData(user)) {
            throw new AuthException("Invalid email/password format");
        }

        final Optional<User> optionalExistingUser = this.userService.findByEmail(user.getEmail());
        if (optionalExistingUser.isPresent()) {
            throw new AuthException("Tried making a user that already exists");
        }

        final Role role = this.roleService.getRoleByName(RoleName.valueOf(roleName))
                .orElseThrow(() -> new AuthException("Tried setting a role that doesn't exist"));
        final Tenant tenant = this.tenantService.findByCode(tenantCode);

        user.setRoles(Set.of(role));
        user.setTenant(tenant);
        user.setOauth(false);
        user.setTfaSecret(this.twoFactorService.generateNewSecret());
        user.setPassword(this.passwordEncoder.encode(user.getPassword().trim()));
        return this.userRepository.save(user);
    }

    @Override
    public User updateUser(final UserDto request, final String tenantCode, final Long adminId) {
        final User user = this.userRepository.findById(adminId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!user.getTenant().getCode().equals(tenantCode)) {
            throw new IllegalArgumentException("Specified tenant is not associated with user");
        }

        final Set<Role> userRoles = user.getRoles();
        Set<RoleName> roleNameSet = userRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        if (roleNameSet.stream().anyMatch(this.authService::canOnlyCRUDUser)) {
            throw new IllegalArgumentException("Only super admin can update admin");
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        if (request.phoneNumber() != null) {
            user.setPhoneNumber(request.phoneNumber());
        }

        return this.userRepository.save(user);
    }

    @Override
    public void removeUser(final String tenantCode, final Long userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!user.getTenant().getCode().equals(tenantCode)) {
            throw new IllegalArgumentException("Specified tenant is not associated with user");
        }

        final Set<Role> userRoles = user.getRoles();
        Set<RoleName> roleNameSet = userRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        if (roleNameSet.stream().anyMatch(this.authService::canOnlyCRUDUser)) {
            throw new IllegalArgumentException("Only super admin can remove admin");
        }

        this.userRepository.deleteById(userId);
    }
}
