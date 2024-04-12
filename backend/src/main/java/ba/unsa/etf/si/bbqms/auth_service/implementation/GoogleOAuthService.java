package ba.unsa.etf.si.bbqms.auth_service.implementation;

import ba.unsa.etf.si.bbqms.auth_service.api.OAuthService;
import ba.unsa.etf.si.bbqms.auth_service.api.RoleService;
import ba.unsa.etf.si.bbqms.domain.Role;
import ba.unsa.etf.si.bbqms.domain.RoleName;
import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.exceptions.AuthException;
import ba.unsa.etf.si.bbqms.repository.UserRepository;
import ba.unsa.etf.si.bbqms.tenant_service.api.TenantService;
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

    @Value("${tenancy.default-code}")
    private String TENANCY_DEFAULT_CODE;

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final TenantService tenantService;

    public GoogleOAuthService(final UserRepository userRepository,
                              final RoleService roleService,
                              final TenantService tenantService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.tenantService = tenantService;
    }

    @Override
    public Optional<User> authenticateUser(final String googleCredentials) {
        try {
            final String userEmail = this.extractToken(googleCredentials);

            final Role role = this.roleService.getRoleByName(RoleName.ROLE_SUPER_ADMIN)
                    .orElseThrow(() -> new AuthException("Tried setting a role that doesn't exist."));

            final Tenant tenant = this.tenantService.findByCode(this.TENANCY_DEFAULT_CODE);

            final User user = this.userRepository.findByEmailEquals(userEmail)
                    .orElseGet(() -> this.userRepository.save(
                            User.builder()
                                    .email(userEmail)
                                    .roles(Set.of(role))
                                    .tenant(tenant)
                                    .oAuth(true)
                                    .build()
                    ));
            return Optional.of(user);
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
