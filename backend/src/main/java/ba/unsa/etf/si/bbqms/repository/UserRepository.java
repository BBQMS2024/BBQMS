package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
