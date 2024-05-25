package ba.unsa.etf.si.bbqms.repository;

import ba.ekapic1.stonebase.BaseRepository;
import ba.unsa.etf.si.bbqms.domain.Branch;

import java.util.Set;


public interface BranchRepository extends BaseRepository<Branch, Long> {
    Set<Branch> findAllByTenant_Code(final String tenantCode);
}
