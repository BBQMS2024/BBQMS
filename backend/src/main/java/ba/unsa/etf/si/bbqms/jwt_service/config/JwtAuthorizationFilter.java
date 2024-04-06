package ba.unsa.etf.si.bbqms.jwt_service.config;

import ba.unsa.etf.si.bbqms.auth_service.api.CookieService;
import ba.unsa.etf.si.bbqms.auth_service.api.UserService;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.exceptions.AuthException;
import ba.unsa.etf.si.bbqms.jwt_service.api.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CookieService cookieService;
    private final UserService userService;

    public JwtAuthorizationFilter(final JwtService jwtService,
                                  final CookieService cookieService,
                                  final UserService userService) {
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            final String token = this.cookieService.extractJwtCookie(request.getCookies())
                    .map(Cookie::getValue)
                    .orElse(null);

            final Claims claims = jwtService.resolveClaims(token);
            if (claims != null && !jwtService.isExpired(claims)) {
                final String username = claims.getSubject();
                final Set<GrantedAuthority> userAuthorities = jwtService.getAuthoritiesFromClaims(claims);

                final User currentUser = this.userService.findByEmail(username)
                        .orElseThrow(() -> new AuthException("No user with username " + username + " found."));

                final Authentication authentication =
                        new UsernamePasswordAuthenticationToken(username, "", userAuthorities);

                final String newToken = this.jwtService.generateToken(currentUser);
                final Cookie newJwtCookie = this.cookieService.generateDefaultJwtCookie(newToken);

                response.addCookie(newJwtCookie);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }

        filterChain.doFilter(request, response);
    }
}
