package ba.unsa.etf.si.bbqms.repository;

import ba.ekapic1.stonebase.BaseRepository;
import ba.unsa.etf.si.bbqms.domain.Display;

import java.util.Set;

public interface DisplayRepository extends BaseRepository<Display, Long> {
    Set<Display> findByBranchId(final long branchId);
    Set<Display> findByBranchTenantCode(String tenantCode);
    Set<Display> findByBranchTenantCodeAndTellerStationIsNull(String tenantCode);
    Set<Display> findAllByBranch_IdAndTellerStationIsNull(final long branchId);
}
