package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByCode(final String code);
}
