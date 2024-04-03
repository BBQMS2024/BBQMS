package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.TellerStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TellerStationRepository extends JpaRepository<TellerStation, Long> {
    Optional<TellerStation> findByDisplayId(final long displayId);
}
