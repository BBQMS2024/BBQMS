package ba.unsa.etf.si.bbqms.ticket_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.queue_service.api.QueueService;
import ba.unsa.etf.si.bbqms.repository.BranchRepository;
import ba.unsa.etf.si.bbqms.repository.ServiceRepository;
import ba.unsa.etf.si.bbqms.repository.TicketRepository;
import ba.unsa.etf.si.bbqms.ticket_service.api.TicketService;
import jakarta.persistence.EntityNotFoundException;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class DefaultTicketService implements TicketService {
    private final TicketRepository ticketRepository;
    private final ServiceRepository serviceRepository;
    private final BranchRepository branchRepository;
    private final BranchService branchService;
    private final QueueService queueService;
    public DefaultTicketService(final TicketRepository ticketRepository,
                                final ServiceRepository serviceRepository,
                                final BranchRepository branchRepository,
                                final BranchService branchService,
                                final QueueService queueService) {
        this.ticketRepository = ticketRepository;
        this.serviceRepository = serviceRepository;
        this.branchRepository = branchRepository;
        this.branchService = branchService;
        this.queueService = queueService;
    }

    @Override
    public Ticket createNewTicket(final long serviceId, final long branchId, final String deviceToken) {
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
    public Set<Ticket> getTicketsByDevice(final String deviceToken) {
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
    public Ticket getTicketById(final long id) {
        return this.ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No ticket with id: " + id));
    }

    @Override
    public void deleteAllTickets() {
        this.ticketRepository.deleteAll();
    }

    @Override
    public void deleteWithIds(final Set<Long> ticketIds) {
        this.ticketRepository.deleteAllById(ticketIds);
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
    public Set<Ticket> getTicketsForServices(Set<Service> services) {
        return ticketRepository.findAllByServiceIn(services);
    }
}
