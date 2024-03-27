package ba.unsa.etf.si.bbqms;

import ba.unsa.etf.si.bbqms.auth_service.api.RoleService;
import ba.unsa.etf.si.bbqms.auth_service.api.UserService;
import ba.unsa.etf.si.bbqms.auth_service.implementation.DefaultAuthService;
import ba.unsa.etf.si.bbqms.domain.Role;
import ba.unsa.etf.si.bbqms.domain.RoleName;
import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.exceptions.AuthException;
import ba.unsa.etf.si.bbqms.tenant_service.api.TenantService;
import ba.unsa.etf.si.bbqms.tfa_service.api.TwoFactorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private TwoFactorService twoFactorService;
    @Mock
    private TenantService tenantService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DefaultAuthService authService;

    @Test
    public void testRegisterUser_WithValidUser_ShouldReturnUser() throws AuthException {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        Role role = new Role();
        role.setName(RoleName.ROLE_SUPER_ADMIN);

        when(roleService.getRoleByName(any())).thenReturn(Optional.of(role));
        when(tenantService.findByCode(any())).thenReturn(new Tenant());
        when(twoFactorService.generateNewSecret()).thenReturn("secret");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userService.save(any())).thenReturn(user);

        User registeredUser = authService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("test@example.com", registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPassword());
    }
}
