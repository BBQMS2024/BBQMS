package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface BranchGroupRepository extends JpaRepository<BranchGroup, Long> {
    List<BranchGroup> findGroupsByTenantCode(final String tenantCode);
    Set<BranchGroup> findAllByServicesContains(final Service service);
}
