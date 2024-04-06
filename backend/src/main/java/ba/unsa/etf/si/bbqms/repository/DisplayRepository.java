package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Display;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DisplayRepository extends JpaRepository<Display, Long> {
    Set<Display> findByBranchId(final long branchId);
    Set<Display> findByBranchTenantCode(String tenantCode);
    Set<Display> findByBranchTenantCodeAndTellerStationIsNull(String tenantCode);
    Set<Display> findAllByBranch_IdAndTellerStationIsNull(final long branchId);
}
