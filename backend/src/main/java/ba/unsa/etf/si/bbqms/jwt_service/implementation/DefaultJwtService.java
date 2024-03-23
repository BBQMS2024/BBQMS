package ba.unsa.etf.si.bbqms.jwt_service.implementation;

import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.jwt_service.api.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultJwtService implements JwtService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultJwtService.class);

    @Value("${jwt.authorities-key}")
    public String AUTHORITIES_KEY;

    @Value("${jwt.secret-key}")
    public String SECRET_KEY;

    @Value("${jwt.token-validity-time}")
    public Duration TOKEN_VALIDITY_TIME;

    @Override
    public String generateToken(final User user) {
        try{
            final String authorities = user.getRoles().stream()
                    .map(role -> role.getName().name())
                    .collect(Collectors.joining(","));

            return Jwts.builder()
                    .subject(user.getEmail())
                    .claim(this.AUTHORITIES_KEY, authorities)
                    .issuedAt(Date.from(Instant.now()))
                    .expiration(Date.from(Instant.now().plus(this.TOKEN_VALIDITY_TIME)))
                    .signWith(getSignInKey())
                    .compact();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

    }

    @Override
    public Claims resolveClaims(final String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public boolean isExpired(final Claims claims) {
        final Instant expiration = claims.getExpiration().toInstant();
        return expiration.isBefore(Instant.now());
    }

    @Override
    public Set<GrantedAuthority> getAuthoritiesFromClaims(final Claims claims) {
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private Key getSignInKey(){
        byte[] keyBytes = this.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
