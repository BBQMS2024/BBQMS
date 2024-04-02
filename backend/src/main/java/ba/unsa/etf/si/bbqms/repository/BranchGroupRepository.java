package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BranchGroupRepository extends JpaRepository<BranchGroup, Long> {
    @Query("select distinct bg from BranchGroup bg join bg.branches b where b.tenant.code = :tenantCode")
    List<BranchGroup> findGroupsByTenantCode(@Param("tenantCode") final String tenantCode);
}
