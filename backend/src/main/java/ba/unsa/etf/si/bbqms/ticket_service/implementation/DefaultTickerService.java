package ba.unsa.etf.si.bbqms.ticket_service.implementation;

import ba.unsa.etf.si.bbqms.ticket_service.api.TicketService;
import org.springframework.stereotype.Service;

@Service
public class DefaultTickerService implements TicketService {
    @Override
    public String dummyTicketServiceMethod() {
        return "Hello tickets app";
    }
}
