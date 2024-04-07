package ba.unsa.etf.si.bbqms.ticket_service.api;

import ba.unsa.etf.si.bbqms.domain.Ticket;

import java.util.Set;

public interface TicketService {
    Ticket createNewTicket(final long serviceId, final long branchId, final String deviceToken);
    Set<Ticket> getTicketsByDevice(final String deviceToken);
    void cancelTicket(final long ticketId);
}
