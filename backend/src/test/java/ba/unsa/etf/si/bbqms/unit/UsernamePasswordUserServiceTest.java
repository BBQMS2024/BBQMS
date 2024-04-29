package ba.unsa.etf.si.bbqms.unit;

import ba.unsa.etf.si.bbqms.auth_service.implementation.UsernamePasswordUserService;
import ba.unsa.etf.si.bbqms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UsernamePasswordUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UsernamePasswordUserService usernamePasswordUserService;

    @Test
    public void testLoadUserByUserName_WhenInvalidEmailFormat_ShouldThrowUsernameNotFoundException() {
        String invalidEmail = "invalid.email";
        assertThrows(UsernameNotFoundException.class, () -> usernamePasswordUserService.loadUserByUsername(invalidEmail));
    }
}
