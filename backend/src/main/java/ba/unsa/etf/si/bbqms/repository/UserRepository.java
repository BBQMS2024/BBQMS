package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailEqualsIgnoreCaseAndOauthEquals(final String email, final boolean isOauth);
}
