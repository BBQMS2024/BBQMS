package ba.unsa.etf.si.bbqms.auth_service.api;

import jakarta.servlet.http.Cookie;

import java.util.Optional;

public interface CookieService {
    Cookie generateDefaultJwtCookie(String token);
    Optional<Cookie> extractJwtCookie(Cookie[] cookies);
}
