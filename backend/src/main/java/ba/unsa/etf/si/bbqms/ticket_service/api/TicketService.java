package ba.unsa.etf.si.bbqms.ticket_service.api;

import ba.unsa.etf.si.bbqms.domain.Ticket;

public interface TicketService {
    Ticket createNewTicket(final long serviceId, final long branchId);
}
