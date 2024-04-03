package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.TellerStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TellerStationRepository extends JpaRepository<TellerStation, Long> {
    List<TellerStation> findByBranch_Tenant_Code(final String tenantCode);
}
