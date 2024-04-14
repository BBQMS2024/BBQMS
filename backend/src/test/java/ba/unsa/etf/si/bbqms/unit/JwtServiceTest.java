package ba.unsa.etf.si.bbqms.unit;

import ba.unsa.etf.si.bbqms.jwt_service.implementation.DefaultJwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    @InjectMocks
    private DefaultJwtService jwtService;

    @Test
    public void testGetAuthoritiesFromClaims_ShouldReturnValidAuthorities() {
        Claims claims = mock(Claims.class);
        when(claims.get("authorities")).thenReturn("ROLE_ADMIN,ROLE_USER");
        jwtService.AUTHORITIES_KEY = "authorities";
        Set<GrantedAuthority> authorities = jwtService.getAuthoritiesFromClaims(claims);

        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
