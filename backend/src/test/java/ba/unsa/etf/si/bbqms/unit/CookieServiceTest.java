package ba.unsa.etf.si.bbqms.unit;

import ba.unsa.etf.si.bbqms.auth_service.implementation.DefaultCookieService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CookieServiceTest {
    @InjectMocks
    private DefaultCookieService cookieService;
    @Test
    public void testGeneratedDefaultJwtCookie_ShouldReturnValidCookie() {
        final String token = "sampleToken";
        cookieService.cookieJwtTitle = "jwt_cookie_title";
        final Cookie jwtCookie = cookieService.generateDefaultJwtCookie(token);

        assertNotNull(jwtCookie);
        assertEquals("jwt_cookie_title", jwtCookie.getName());
        assertEquals(token, jwtCookie.getValue());
        assertTrue(jwtCookie.isHttpOnly());
        assertEquals(1800, jwtCookie.getMaxAge());
    }
}
