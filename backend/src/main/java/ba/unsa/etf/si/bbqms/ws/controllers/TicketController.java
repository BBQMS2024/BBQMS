package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.ticket_service.api.TicketService;
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import ba.unsa.etf.si.bbqms.ws.models.TellerStationDto;
import ba.unsa.etf.si.bbqms.ws.models.TicketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
    private final TicketService ticketService;
    private final BranchService branchService;

    public TicketController(final TicketService ticketService, final BranchService branchService) {
        this.ticketService = ticketService;
        this.branchService = branchService;
    }

    /**
     * Returns ticket object, as well as all stations to which the visitor can potentially be routed to.
     */
    @PostMapping
    public ResponseEntity createNewTicket(@RequestBody final NewTicketRequest request) {
        try {
            final Ticket created = this.ticketService.createNewTicket(request.serviceId(), request.branchId(), request.deviceToken());
            final Set<TellerStationDto> legibleStations = this.branchService.getStationsWithService(created.getBranch(), created.getService()).stream()
                    .map(TellerStationDto::fromEntity)
                    .collect(Collectors.toSet());

            return ResponseEntity.ok().body(new TicketResponse(
                    TicketDto.fromEntity(created),
                    legibleStations
            ));
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/devices/{deviceToken}")
    public ResponseEntity getTicketsForDevice(@PathVariable final String deviceToken) {

        final Set<TicketResponse> responses = new HashSet<>();
        for (final Ticket ticket : this.ticketService.getTicketsByDevice(deviceToken)) {
            final Set<TellerStationDto> legibleStations = this.branchService.getStationsWithService(ticket.getBranch(), ticket.getService()).stream()
                    .map(TellerStationDto::fromEntity)
                    .collect(Collectors.toSet());

            responses.add(new TicketResponse(
                    TicketDto.fromEntity(ticket),
                    legibleStations
            ));
        }

        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity cancelTicket(@PathVariable final String ticketId) {
        try {
            ticketService.cancelTicket(Long.parseLong(ticketId));
            return ResponseEntity.ok().body(new SimpleMessageDto("Deleted ticket with id: " + ticketId));
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    public record NewTicketRequest(long branchId, long serviceId, String deviceToken) {
    }

    public record TicketResponse(TicketDto ticket, Set<TellerStationDto> stations) {
    }
}
