package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailEqualsAndOauthEquals(final String email, final boolean isOauth);
    Optional<User> findByEmailEquals(final String email);
}
