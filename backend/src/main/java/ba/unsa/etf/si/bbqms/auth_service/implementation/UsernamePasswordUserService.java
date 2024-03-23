package ba.unsa.etf.si.bbqms.auth_service.implementation;

import ba.unsa.etf.si.bbqms.auth_service.api.UserService;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.repository.UserRepository;
import ba.unsa.etf.si.bbqms.utils.EmailValidator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsernamePasswordUserService implements UserService {
    private final UserRepository userRepository;

    public UsernamePasswordUserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(final String email) throws UsernameNotFoundException {
        if (!EmailValidator.validate(email)) {
            throw new UsernameNotFoundException("Invalid email format");
        }

        return this.userRepository.findByEmailEqualsIgnoreCaseAndOauthEquals(email, false)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email: " + email + " found"));
    }

    @Override
    public User save(final User user) {
        return this.userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return this.userRepository.findByEmailEqualsIgnoreCaseAndOauthEquals(email, false);
    }
}
