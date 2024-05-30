package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.StationService;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.queue_service.api.QueueService;
import ba.unsa.etf.si.bbqms.repository.TicketRepository;
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import ba.unsa.etf.si.bbqms.ws.models.TicketDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/teller")
public class TellerController {
    private static final Logger logger = LoggerFactory.getLogger(TellerController.class);
    private final QueueService queueService;
    private final StationService stationService;

    public TellerController(final QueueService queueService, final StationService stationService) {
        this.queueService = queueService;
        this.stationService = stationService;
    }

    @PostMapping("/advance-queue/{stationId}")
    public ResponseEntity advanceQueueState(@PathVariable final String stationId) {
        try {
            final TellerStation station = this.stationService.findById(Long.parseLong(stationId))
                    .orElseThrow(() -> new EntityNotFoundException("No station with id: " + stationId));

            final Optional<Ticket> nextTicket = this.queueService.advanceQueueState(station);
            if (nextTicket.isEmpty()) {
                return ResponseEntity.ok().body(
                        new SimpleMessageDto("Queue for this station is empty.")
                );
            }

            return ResponseEntity.ok().body(TicketDto.fromEntity(nextTicket.get()));
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/current-ticket/{stationId}")
    public ResponseEntity returnCurrentTicket(@PathVariable final String stationId) {
        try {
            final TellerStation station = this.stationService.findById(Long.parseLong(stationId))
                    .orElseThrow(() -> new EntityNotFoundException("No station with id: " + stationId));

            final Optional<Ticket> currentTicket = this.queueService.findCurrentTicketForStation(station);
            if (currentTicket.isEmpty()) {
                return ResponseEntity.ok().body(new SimpleMessageDto("No ticket assigned to this station."));
            }
            return ResponseEntity.ok().body(TicketDto.fromEntity(currentTicket.get()));
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/undo-queue/{stationId}")
    public ResponseEntity undoLastTicket(@PathVariable final String stationId) {
        try{
            final Optional<Ticket> optionalNextTicket = this.queueService.undoStationState(Long.parseLong(stationId));

            if (optionalNextTicket.isPresent()) {
                return ResponseEntity.ok().body(TicketDto.fromEntity(optionalNextTicket.get()));
            } else {
                return ResponseEntity.ok().body(new SimpleMessageDto("No next ticket."));
            }

        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
