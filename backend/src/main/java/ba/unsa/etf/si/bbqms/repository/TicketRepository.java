package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findTopByServiceInAndBranchOrderByNumberDesc(final Set<Service> service, final Branch branch);
    Optional<Ticket> findTopByServiceInAndBranchAndTellerStationIsNullOrderByCreatedAtAsc(final Set<Service> service, final Branch branch);
    void deleteByTellerStation(final TellerStation tellerStation);
    Set<Ticket> findAllByDeviceToken(final String deviceToken);
    Set<Ticket> findAllByServiceIn(final Set<Service> services);
    Set<Ticket> findAllByBranch_Id(final long branchId);
    Set<Ticket> findAllByServiceInAndBranch_Id(final Set<Service> services, final long branchId);
    Set<Ticket> findAllByService_IdAndBranch_Id(final long serviceId, final long branchId);
}
