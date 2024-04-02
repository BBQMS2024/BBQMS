package ba.unsa.etf.si.bbqms.auth_service.implementation;

import ba.unsa.etf.si.bbqms.auth_service.api.CookieService;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class DefaultCookieService implements CookieService {

    public String cookieJwtTitle;

    @Override
    public Cookie generateDefaultJwtCookie(final String token) {
        Cookie jwtCookie = new Cookie(cookieJwtTitle, token);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(60*30); //in seconds, equal to 30 minutes

        return jwtCookie;
    }

    @Override
    public Optional<Cookie> extractJwtCookie(final Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieJwtTitle))
                .findFirst();
    }
}
