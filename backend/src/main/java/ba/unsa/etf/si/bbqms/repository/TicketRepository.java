package ba.unsa.etf.si.bbqms.repository;

import ba.ekapic1.stonebase.BaseRepository;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.Ticket;

import java.util.Set;

public interface TicketRepository extends BaseRepository<Ticket, Long> {
    Set<Ticket> findAllByServiceInAndBranch(final Set<Service> service, final Branch branch);
    Set<Ticket> findAllByDeviceToken(final String deviceToken);
    Set<Ticket> findAllByServiceIn(final Set<Service> services);
    Set<Ticket> findAllByBranch_Id(final long branchId);
    Set<Ticket> findAllByServiceInAndBranch_Id(final Set<Service> services, final long branchId);
}
