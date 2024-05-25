package ba.unsa.etf.si.bbqms.repository;

import ba.ekapic1.stonebase.BaseRepository;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Service;

import java.util.Set;

public interface BranchGroupRepository extends BaseRepository<BranchGroup, Long> {
    Set<BranchGroup> findAllByServicesContains(final Service service);
}
