package ba.unsa.etf.si.bbqms.ticket_service.api;

import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

public interface TicketService {
    Ticket createNewTicket(final long serviceId, final long branchId, final String deviceToken);
    Set<Ticket> getTicketsByDevice(final String deviceToken);
    void cancelTicket(final long ticketId);
    Ticket getTicketById(final long id);
    void deleteAllTickets();
    void deleteWithIds(final Set<Long> ticketIds);
    List<Ticket> findAllByServicesAndBranch(final Set<Service> services, final Branch branch, final Sort sort);
}
