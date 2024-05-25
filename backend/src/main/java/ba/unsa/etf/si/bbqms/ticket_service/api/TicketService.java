package ba.unsa.etf.si.bbqms.ticket_service.api;

import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.utils.Wrapper;

import java.util.List;
import java.util.Set;

public interface TicketService extends Wrapper {
    Ticket get(final long id);

    Ticket create(final long serviceId, final long branchId, final String deviceToken);

    Set<Ticket> getByDevice(final String deviceToken);

    void cancelTicket(final long ticketId);

    void deleteAll();

    default List<Ticket> findWithStation(final long stationId) {
        return findWithStation(stationId, false);
    }

    List<Ticket> findWithStation(final long stationId, final boolean deleted);

    Set<Ticket> getTicketsForServicesAtBranch(final Set<Service> services, final long branchId);
}
