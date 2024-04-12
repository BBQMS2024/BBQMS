package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.BranchGroup;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BranchGroupRepository extends JpaRepository<BranchGroup, Long> {
    @Query("select distinct bg from BranchGroup bg join bg.branches b where b.tenant.code = :tenantCode")
    List<BranchGroup> findGroupsByTenantCode(@Param("tenantCode") final String tenantCode);
=======
import ba.unsa.etf.si.bbqms.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface BranchGroupRepository extends JpaRepository<BranchGroup, Long> {
    List<BranchGroup> findGroupsByTenantCode(final String tenantCode);
    Set<BranchGroup> findAllByServicesContains(final Service service);
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
}
