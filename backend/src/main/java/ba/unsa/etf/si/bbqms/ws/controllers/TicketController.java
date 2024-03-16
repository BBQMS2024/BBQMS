package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.ticket_service.api.TicketService;
import ba.unsa.etf.si.bbqms.ws.models.DummyDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public DummyDto example() {
        String example = ticketService.dummyTicketServiceMethod();
        return new DummyDto(example);
    }
}
