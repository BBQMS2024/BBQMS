package ba.unsa.etf.si.bbqms.ticket_service.implementation;

import ba.ekapic1.stonebase.filter.CompositeField;
import ba.ekapic1.stonebase.filter.ConditionType;
import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.BranchField;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.ServiceField;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.TellerStationField;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.domain.TicketField;
import ba.unsa.etf.si.bbqms.queue_service.api.QueueService;
import ba.unsa.etf.si.bbqms.repository.BranchRepository;
import ba.unsa.etf.si.bbqms.repository.ServiceRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import ba.unsa.etf.si.bbqms.repository.TicketRepository;
import ba.unsa.etf.si.bbqms.ticket_service.api.TicketService;
import jakarta.persistence.EntityNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class DefaultTicketService implements TicketService {
    private final TicketRepository ticketRepository;
    private final ServiceRepository serviceRepository;
    private final BranchRepository branchRepository;
    private final TellerStationRepository tellerStationRepository;
    private final BranchService branchService;
    private final QueueService queueService;
    public DefaultTicketService(final TicketRepository ticketRepository,
                                final ServiceRepository serviceRepository,
                                final BranchRepository branchRepository,
                                final TellerStationRepository tellerStationRepository,
                                final BranchService branchService,
                                final QueueService queueService) {
        this.ticketRepository = ticketRepository;
        this.serviceRepository = serviceRepository;
        this.branchRepository = branchRepository;
        this.tellerStationRepository = tellerStationRepository;
        this.branchService = branchService;
        this.queueService = queueService;
    }

    @Override
    public Ticket create(final long serviceId, final long branchId, final String deviceToken) {
        final Service service = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("No service with id: " + serviceId));

        final Branch branch = this.branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("No branch with id: " + branchId));

        final Set<Service> possibleServices = this.branchService.extractPossibleServices(branch);
        if (!possibleServices.contains(service)) {
            throw new IllegalArgumentException("Branch with id: " + branchId + " does not offer service with id: " + serviceId);
        }

        int serviceIndex = 0;
        for (Service possibleService : possibleServices) {
            if (possibleService.getId() == serviceId) {
                break;
            }
            serviceIndex++;
        }

        final char serviceLetter = (char) ('A' + serviceIndex);
        final String formattedNumber =  serviceLetter + String.valueOf(getNextTicketNumber(possibleServices, branch));
        final Ticket newTicket = new Ticket(formattedNumber, Instant.now(), deviceToken, service, branch);

        return this.ticketRepository.save(newTicket);
    }

    @Override
    public Set<Ticket> getByDevice(final String deviceToken) {
        return this.ticketRepository.findAllByDeviceToken(deviceToken);
    }

    @Override
    public void cancelTicket(final long ticketId) {
        final Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("No ticket with id: " + ticketId));

        final TellerStation tellerStation = ticket.getTellerStation();
        if(tellerStation != null) {
            this.queueService.advanceQueueState(tellerStation);
        }
        else {
            this.ticketRepository.delete(ticket);
        }
    }

    @Override
    public Ticket get(final long id) {
        return this.ticketRepository.get(id);
    }

    @Override
    public void deleteAll() {
        this.ticketRepository.deleteAll();
    }

    @Override
    public List<Ticket> findWithStation(final long stationId, final boolean deleted) {
        final TellerStation tellerStation = this.tellerStationRepository.get(
                this.tellerStationRepository.filterBuilder()
                        .with(TellerStationField.ID, ConditionType.EQUAL, stationId)
                        .build()
        );

        return this.ticketRepository.findAll(
                this.ticketRepository.filterBuilder()
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
                        .with(TicketField.DELETED, ConditionType.EQUAL, deleted)
                        .withOrderAsc(TicketField.CREATED_AT)
                        .build()
        );
    }


    private long getNextTicketNumber(final Set<Service> possibleServices, final Branch branch) {
        final Set<String> ticketNumbers = ticketRepository.findAllByServiceInAndBranch(possibleServices, branch)
                .stream()
                .map(Ticket::getNumber)
                .map(this::extractNumericPart)
                .collect(Collectors.toSet());

        final long maxNumber = ticketNumbers.isEmpty() ? 0 : ticketNumbers.stream().mapToLong(Long::parseLong).max().getAsLong();

        return maxNumber + 1;
    }

    private String extractNumericPart(final String ticketNumber) {
        return ticketNumber.replaceAll("[^0-9]", "");
    }

    @Override
    public Set<Ticket> getTicketsForServicesAtBranch(final Set<Service> services, final long branchId) {
        return this.ticketRepository.findAllByServiceInAndBranch_Id(services, branchId);
    }

    @Override
    public <T> T unwrap(final Class<T> tClass) {
        if (tClass.isAssignableFrom(TicketRepository.class)) {
            return tClass.cast(this.ticketRepository);
        }

        return TicketService.super.unwrap(tClass);
    }
}
