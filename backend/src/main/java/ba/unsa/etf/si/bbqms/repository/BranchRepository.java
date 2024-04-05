package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface BranchRepository extends JpaRepository<Branch, Long> {
    Set<Branch> findAllByTenant_Code(final String tenantCode);
}
