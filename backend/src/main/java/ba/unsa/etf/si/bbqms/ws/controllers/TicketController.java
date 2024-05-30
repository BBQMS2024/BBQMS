package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.ekapic1.stonebase.filter.CompositeField;
import ba.ekapic1.stonebase.filter.ConditionType;
import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.domain.TellerStationField;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.domain.TicketField;
import ba.unsa.etf.si.bbqms.export.api.Exporter;
import ba.unsa.etf.si.bbqms.repository.TicketRepository;
import ba.unsa.etf.si.bbqms.ticket_service.api.TicketService;
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import ba.unsa.etf.si.bbqms.ws.models.TellerStationDto;
import ba.unsa.etf.si.bbqms.ws.models.TicketDto;
import com.google.common.net.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
    private final TicketService ticketService;
    private final BranchService branchService;
    private final Exporter<Ticket> pdfExporter;

    public TicketController(final TicketService ticketService,
                            final BranchService branchService,
                            final Exporter<Ticket> pdfExporter) {
        this.ticketService = ticketService;
        this.branchService = branchService;
        this.pdfExporter = pdfExporter;
    }

    /**
     * Returns ticket object, as well as all stations to which the visitor can potentially be routed to.
     */
    @PostMapping
    public ResponseEntity createNewTicket(@RequestBody final NewTicketRequest request) {
        try {
            final Ticket created = this.ticketService.create(request.serviceId(), request.branchId(), request.deviceToken());
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

    @GetMapping("/{ticketId}")
    public ResponseEntity getTicketById(@PathVariable final String ticketId) {
       try {
            final Ticket ticket = this.ticketService.get(Long.parseLong(ticketId));
            return ResponseEntity.ok().body(TicketDto.fromEntity(ticket));
       } catch (final Exception exception) {
           logger.error(exception.getMessage());
           return ResponseEntity.badRequest().build();
       }
    }

    @GetMapping("/devices/{deviceToken}")
    public ResponseEntity getTicketsForDevice(@PathVariable final String deviceToken) {

        final Set<TicketResponse> responses = new HashSet<>();
        for (final Ticket ticket : this.ticketService.getByDevice(deviceToken)) {
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
            this.ticketService.cancelTicket(Long.parseLong(ticketId));
            return ResponseEntity.ok().body(new SimpleMessageDto("Deleted ticket with id: " + ticketId));
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{ticketId}/print")
    public ResponseEntity printTicket(@PathVariable final String ticketId) {
        try{
            final Ticket ticket = this.ticketService.get(Long.parseLong(ticketId));
            final Resource pdfByteArray = this.pdfExporter.exportPdf(ticket);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ticket.getId() + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfByteArray);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("test")
    public List<TicketDto> test() {
        final TicketRepository ticketRepository = this.ticketService.unwrap(TicketRepository.class);

        Specification<Ticket> filter = ticketRepository.filterBuilder()
                .with(CompositeField.of(TicketField.TELLER_STATION, TellerStationField.ID), ConditionType.EQUAL, null)
                .withOrderAsc(TicketField.CREATED_AT)
                .build();

        return ticketRepository.findAll(filter
        ).stream().map(TicketDto::fromEntity).toList();
    }

    public record NewTicketRequest(long branchId, long serviceId, String deviceToken) {
    }

    public record TicketResponse(TicketDto ticket, Set<TellerStationDto> stations) {
    }
}
