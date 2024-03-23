package ba.unsa.etf.si.bbqms.auth_service.implementation;

import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.auth_service.api.RoleService;
import ba.unsa.etf.si.bbqms.auth_service.api.UserService;
import ba.unsa.etf.si.bbqms.domain.Role;
import ba.unsa.etf.si.bbqms.domain.RoleName;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.exceptions.AuthException;
import ba.unsa.etf.si.bbqms.jwt_service.api.JwtService;
import ba.unsa.etf.si.bbqms.tfa_service.api.TwoFactorService;
import ba.unsa.etf.si.bbqms.utils.EmailValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class DefaultAuthService implements AuthService {
    private final UserService userService;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final TwoFactorService twoFactorService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public DefaultAuthService(final UserService userService,
                              final RoleService roleService,
                              final JwtService jwtService,
                              final TwoFactorService twoFactorService,
                              final AuthenticationManager authenticationManager,
                              final PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.jwtService = jwtService;
        this.twoFactorService = twoFactorService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(final User user) throws AuthException {
        if (!EmailValidator.validate(user.getEmail()) ||
                user.getPassword() == null ||
                user.getPassword().length() < 4) {
            throw new AuthException("Invalid email/password format.");
        }

        final Optional<User> optionalExistingUser = this.userService.findByEmail(user.getEmail());
        if (optionalExistingUser.isPresent()) {
            throw new AuthException("Tried making a user that already exists.");
        }

        final Role role = this.roleService.getRoleByName(RoleName.ROLE_SUPER_ADMIN)
                .orElseThrow(() -> new AuthException("Tried setting a role that doesn't exist."));//nek za sad svi budu super admin

        user.setRoles(Set.of(role));
        user.setOauth(false);
        user.setTfaSecret(this.twoFactorService.generateNewSecret());
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        return userService.save(user);
    }

    @Override
    public Optional<User> loginUser(final User userData) {
        final Authentication authentication =
                this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userData.getEmail(), userData.getPassword()));
        return this.userService.findByEmail(authentication.getName());
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return this.userService.findByEmail(email);
    }

    @Override
    public String generateUserToken(final User user) {
        return this.jwtService.generateToken(user);
    }

    @Override
    public Optional<User> getCurrentUser() {
        final String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findByEmail(currentEmail);
    }

    @Override
    public String generateUserQrCode(final User user) {
        return this.twoFactorService.generateQrCodeUri(user.getTfaSecret())
                .orElseThrow();
    }

    @Override
    public boolean verifyUserTfaCode(final User user, final String code) {
        try {
            return this.twoFactorService.isCodeValid(code, user.getTfaSecret());
        } catch(final Exception e) {
            return false;
        }
    }
}
