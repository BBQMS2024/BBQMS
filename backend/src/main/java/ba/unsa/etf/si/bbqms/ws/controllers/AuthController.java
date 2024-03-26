package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.auth_service.api.CookieService;
import ba.unsa.etf.si.bbqms.auth_service.api.OAuthService;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.ws.models.ErrorMessage;
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import ba.unsa.etf.si.bbqms.ws.models.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final CookieService cookieService;
    private final OAuthService oAuthService;

    public AuthController(final AuthService authService,
                          final CookieService cookieService,
                          final OAuthService oAuthService) {
        this.authService = authService;
        this.cookieService = cookieService;
        this.oAuthService = oAuthService;
    }

    @PostMapping("/register")
    @PreAuthorize("!isAuthenticated()")
    public ResponseEntity register(@RequestBody final RegisterRequest request) {
        final User newUser = User.builder()
                .password(request.password())
                .email(request.email)
                .build();
        try {
            final User user = this.authService.registerUser(newUser);
            return ResponseEntity.ok(UserDto.fromEntity(user));
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity.badRequest().body(new ErrorMessage("Couldn't register user: " + newUser.getUsername()));
        }
    }

    @PostMapping("/login")
    @PreAuthorize("!isAuthenticated()")
    public ResponseEntity login(@RequestBody final LoginRequest loginRequest) {
        final User userData = User.builder()
                .email(loginRequest.email().trim())
                .password(loginRequest.password().trim())
                .build();

        final Optional<User> optionalUser = this.authService.loginUser(userData);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(UserDto.fromEntity(optionalUser.get()));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity verifyToken() {
        return ResponseEntity.ok(this.authService.getCurrentUser().isPresent());
    }

    @GetMapping("/tfa")
    public ResponseEntity getQrCode(@RequestParam final String email) {
        final Optional<User> optionalUser = this.authService.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String qrCodeUri = this.authService.generateUserQrCode(optionalUser.get());

        return ResponseEntity.ok(new QrCodeResponse(qrCodeUri));
    }

    @PostMapping("/tfa")
    public ResponseEntity verifyCode(final HttpServletResponse response,
                                     @RequestBody final TfaCodeVerificationRequest request) {
        final Optional<User> optionalUser = this.authService.findByEmail(request.email());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        final User user = optionalUser.get();

        final boolean verified = this.authService.verifyUserTfaCode(user, request.code());
        if (verified) {
            final String token = this.authService.generateUserToken(user);
            final Cookie jwtCookie = this.cookieService.generateDefaultJwtCookie(token);
            response.addCookie(jwtCookie);

            return ResponseEntity.ok(new SimpleMessageDto("Code verified successfully."));
        } else {
            return ResponseEntity.badRequest().body(new ErrorMessage("Could not verify the code."));
        }
    }

    @PostMapping("/login/oauth2/google")
    public ResponseEntity loginWithGoogle(final HttpServletResponse response,
                                          @RequestBody final GoogleLoginRequest request) {
        final Optional<User> optionalUser = this.oAuthService.authenticateUser(request.googleToken());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            final User user = optionalUser.get();
            final String userToken = this.authService.generateUserToken(user);
            final Cookie jwtCookie = this.cookieService.generateDefaultJwtCookie(userToken);
            response.addCookie(jwtCookie);

            return ResponseEntity.ok().body(UserDto.fromEntity(user));
        }
    }

    public record RegisterRequest(String email, String password) {
    }

    public record LoginRequest(String email, String password) {
    }

    public record TfaCodeVerificationRequest(String code, String email) {
    }

    public record QrCodeResponse(String qrCode) {
    }

    public record GoogleLoginRequest(String googleToken) {
    }
}
