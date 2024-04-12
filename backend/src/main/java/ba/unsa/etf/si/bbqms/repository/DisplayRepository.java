package ba.unsa.etf.si.bbqms.repository;

<<<<<<< HEAD
=======
import ba.unsa.etf.si.bbqms.domain.Branch;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
import ba.unsa.etf.si.bbqms.domain.Display;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DisplayRepository extends JpaRepository<Display, Long> {
    Set<Display> findByBranchId(final long branchId);
    Set<Display> findByBranchTenantCode(String tenantCode);
    Set<Display> findByBranchTenantCodeAndTellerStationIsNull(String tenantCode);
<<<<<<< HEAD
=======
    Set<Display> findAllByBranch_IdAndTellerStationIsNull(final long branchId);
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
}
