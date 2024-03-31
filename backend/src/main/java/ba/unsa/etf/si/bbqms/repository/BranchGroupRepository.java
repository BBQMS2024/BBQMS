package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchGroupRepository extends JpaRepository<BranchGroup, Long> {
    Optional<BranchGroup> findBranchGroupByName(String name);
}
