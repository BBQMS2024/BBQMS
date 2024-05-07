package ba.unsa.etf.si.bbqms.queue_service.implementation;

import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.notification_service.api.Notification;
import ba.unsa.etf.si.bbqms.notification_service.api.NotificationService;
import ba.unsa.etf.si.bbqms.queue_service.api.QueueService;
import ba.unsa.etf.si.bbqms.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultQueueService implements QueueService {
    private final TicketRepository ticketRepository;
    private final NotificationService notificationService;

    public DefaultQueueService(final TicketRepository ticketRepository, final NotificationService notificationService) {
        this.ticketRepository = ticketRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Optional<Ticket> findNextTicketForStation(final TellerStation tellerStation) {
        return this.ticketRepository.findTopByServiceInAndBranchAndTellerStationIsNullOrderByCreatedAtAsc(tellerStation.getServices(), tellerStation.getBranch());
    }

    @Override
    public void removeActiveTicketFromQueue(final TellerStation tellerStation) {
        this.ticketRepository.deleteByTellerStation(tellerStation);
    }

    @Override
    @Transactional
    public Optional<Ticket> advanceQueueState(final TellerStation tellerStation) {
        // First delete the ticket currently on teller station
        this.ticketRepository.deleteByTellerStation(tellerStation);

        final Optional<Ticket> optionalNextTicket = this.findNextTicketForStation(tellerStation);
        if (optionalNextTicket.isEmpty()) {
            // There is no next ticket. Queue is empty.
            return Optional.empty();
        }

        final Ticket nextTicket = optionalNextTicket.get();
        nextTicket.setTellerStation(tellerStation);

        // Send the notification to whoever is holding the ticket next up.
        final Notification newNotification = new Notification(
                nextTicket.getDeviceToken(),
                "It's your turn!",
                "Please approach station: " + nextTicket.getTellerStation().getName()
        );
        this.notificationService.sendNotification(newNotification);

        return Optional.of(this.ticketRepository.save(nextTicket));
    }

    @Override
    public Optional<Ticket> findCurrentTicketForStation(final TellerStation tellerStation) {
        return this.ticketRepository.findByTellerStation(tellerStation);
    }
}
