package ba.unsa.etf.si.bbqms.repository;

<<<<<<< HEAD
=======
import ba.unsa.etf.si.bbqms.domain.Service;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TellerStationRepository extends JpaRepository<TellerStation, Long> {
    List<TellerStation> findByBranch_Tenant_Code(final String tenantCode);
    Set<TellerStation> findAllByBranch_Id(final long branchId);
<<<<<<< HEAD
=======
    Set<TellerStation> findAllByServicesContains(final Service service);
    Set<TellerStation> findAllByBranch_IdAndServicesContains(final long branchId, final Set<Service> services);
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
    Optional<TellerStation> findByDisplayId(final long displayId);
}
