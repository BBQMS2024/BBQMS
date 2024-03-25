package ba.unsa.etf.si.bbqms.auth_service.implementation;

import ba.unsa.etf.si.bbqms.auth_service.api.OAuthService;
import ba.unsa.etf.si.bbqms.domain.Role;
import ba.unsa.etf.si.bbqms.domain.RoleName;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class GoogleOAuthService implements OAuthService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    private final UserRepository userRepository;

    public GoogleOAuthService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> authenticateUser(final String googleCredentials) {
        try {
            final String userEmail = this.extractToken(googleCredentials);
            final User user = this.userRepository.findByEmailEquals(userEmail)
                    .orElseGet(() -> this.userRepository.save(
                            User.builder()
                                    .email(userEmail)
                                    .roles(Set.of(new Role(RoleName.ROLE_SUPER_ADMIN)))
                                    .oAuth(true)
                                    .build()
                    ));
            return user.isOauth() ? Optional.of(user) : Optional.empty();
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    private String extractToken(final String googleToken) throws GeneralSecurityException, IOException {
        final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(this.GOOGLE_CLIENT_ID))
                .build();

        final GoogleIdToken idToken = verifier.verify(googleToken);
        return idToken.getPayload().getEmail();
    }
}
