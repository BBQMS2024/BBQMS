package ba.unsa.etf.si.bbqms.repository;

import ba.ekapic1.stonebase.BaseRepository;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Service;

import java.util.List;
import java.util.Set;

public interface BranchGroupRepository extends BaseRepository<BranchGroup, Long> {
    List<BranchGroup> findGroupsByTenantCode(final String tenantCode);
    Set<BranchGroup> findAllByServicesContains(final Service service);
}
