package ba.unsa.etf.si.bbqms.queue_service.implementation;

import ba.ekapic1.stonebase.filter.CompositeField;
import ba.ekapic1.stonebase.filter.ConditionType;
import ba.unsa.etf.si.bbqms.domain.BranchField;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.ServiceField;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.TellerStationField;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.domain.TicketField;
import ba.unsa.etf.si.bbqms.notification_service.api.Notification;
import ba.unsa.etf.si.bbqms.notification_service.api.NotificationService;
import ba.unsa.etf.si.bbqms.queue_service.api.QueueService;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import ba.unsa.etf.si.bbqms.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@org.springframework.stereotype.Service
public class DefaultQueueService implements QueueService {
    private final TicketRepository ticketRepository;
    private final TellerStationRepository tellerStationRepository;
    private final NotificationService notificationService;

    public DefaultQueueService(final TicketRepository ticketRepository,
                               final TellerStationRepository tellerStationRepository,
                               final NotificationService notificationService) {
        this.ticketRepository = ticketRepository;
        this.tellerStationRepository = tellerStationRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Optional<Ticket> findNextTicketForStation(final TellerStation tellerStation) {
        final Specification<Ticket> filter = this.ticketRepository.filterBuilder()
                .with(
                        CompositeField.of(TicketField.SERVICE, ServiceField.ID),
                        ConditionType.IN,
                        tellerStation.getServices().stream().map(Service::getId).toList()
                )
                .with(
                        CompositeField.of(TicketField.BRANCH, BranchField.ID),
                        ConditionType.EQUAL,
                        tellerStation.getBranch().getId()
                )
                .with(
                        CompositeField.of(TicketField.TELLER_STATION, TellerStationField.ID),
                        ConditionType.EQUAL,
                        null
                )
                .with(TicketField.DELETED, ConditionType.EQUAL, false)
                .withOrderAsc(TicketField.CREATED_AT)
                .build();

        return this.ticketRepository.findAll(filter, Pageable.ofSize(1)).getContent().stream().findFirst();
    }

    @Override
    @Transactional
    public Optional<Ticket> advanceQueueState(final TellerStation tellerStation) {
        final Optional<Ticket> optionalTicketAtStation = this.ticketRepository.findOne(
                this.ticketRepository.filterBuilder()
                        .with(TicketField.DELETED, ConditionType.EQUAL, false)
                        .with(
                                CompositeField.of(TicketField.TELLER_STATION, TellerStationField.ID),
                                ConditionType.EQUAL,
                                tellerStation.getId()
                        )
                        .build()
        );

        if (optionalTicketAtStation.isPresent()) {
            final Ticket ticketAtStation = optionalTicketAtStation.get();
            ticketAtStation.setDeleted(true);
            ticketRepository.save(ticketAtStation);
        }

        final Optional<Ticket> optionalNextTicket = findNextTicketForStation(tellerStation);
        if (optionalNextTicket.isEmpty()) {
            // There is no next ticket. Queue is empty.
            return Optional.empty();
        }

        final Ticket nextTicket = optionalNextTicket.get();
        nextTicket.setTellerStation(tellerStation);

        // Send the notification to whoever is holding the next ticket.
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
        return this.ticketRepository.findOne(
                this.ticketRepository.filterBuilder()
                        .with(
                                CompositeField.of(TicketField.TELLER_STATION, TellerStationField.ID),
                                ConditionType.EQUAL,
                                tellerStation.getId()
                        )
                        .with(TicketField.DELETED, ConditionType.EQUAL, false)
                        .build()
        );
    }

    @Override
    public Optional<Ticket> undoStationState(final long stationId) {
        final TellerStation station = this.tellerStationRepository.get(stationId);

        final Optional<Ticket> optionalCurrentlyAssigned = findCurrentTicketForStation(station);

        if (optionalCurrentlyAssigned.isEmpty()) {
            throw new IllegalStateException("Detected attempt to undo station state to previous ticket while there is no current ticket. Nothing to undo.");
        }

        final Ticket currentlyAssigned = optionalCurrentlyAssigned.get();

        // De-assign the current ticket which is on the station
        currentlyAssigned.setTellerStation(null);
        this.ticketRepository.save(currentlyAssigned);

        final Specification<Ticket> previousTicketFilter = this.ticketRepository.filterBuilder()
                .with(
                        CompositeField.of(TicketField.TELLER_STATION, TellerStationField.ID),
                        ConditionType.EQUAL,
                        station.getId()
                )
                .with(TicketField.DELETED, ConditionType.EQUAL, true)
                .with(TicketField.CREATED_AT, ConditionType.LESS_THAN, currentlyAssigned.getCreatedAt())
                .withOrderDesc(TicketField.CREATED_AT)
                .build();

        final Optional<Ticket> optionalPreviousTicket = this.ticketRepository.findAll(previousTicketFilter, Pageable.ofSize(1))
                .getContent().stream().findFirst();

        if (optionalPreviousTicket.isPresent()) {
            final Ticket previousTicket = optionalPreviousTicket.get();

            // We "return" the previous ticket and position it to the teller station.
            previousTicket.setDeleted(false);
            previousTicket.setTellerStation(station);

            this.ticketRepository.save(previousTicket);
            return Optional.of(previousTicket);
        }

        return Optional.empty();
    }
}
