package ba.unsa.etf.si.bbqms.jwt_service.api;

import ba.unsa.etf.si.bbqms.domain.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public interface JwtService {
    String generateToken(final User user);

    Claims resolveClaims(final String token);

    boolean isExpired(final Claims claims);

    Set<GrantedAuthority> getAuthoritiesFromClaims(final Claims claims);
}
