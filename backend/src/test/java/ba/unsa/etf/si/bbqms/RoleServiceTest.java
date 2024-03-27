package ba.unsa.etf.si.bbqms;

import ba.unsa.etf.si.bbqms.auth_service.implementation.DefaultRoleService;
import ba.unsa.etf.si.bbqms.domain.Role;
import ba.unsa.etf.si.bbqms.domain.RoleName;
import ba.unsa.etf.si.bbqms.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private DefaultRoleService defaultRoleService;

    @Test
    public void testGetRoleByName_WhenRoleExists_ShouldReturnRole() {
        final RoleName roleName = RoleName.ROLE_SUPER_ADMIN;
        final Role dummyRole = new Role(roleName);
        when(roleRepository.findByName(RoleName.ROLE_SUPER_ADMIN)).thenReturn(Optional.of(dummyRole));

        Optional<Role> optionalRole = defaultRoleService.getRoleByName(roleName);

        assertTrue(optionalRole.isPresent());
        assertEquals(dummyRole, optionalRole.get());
    }
}
