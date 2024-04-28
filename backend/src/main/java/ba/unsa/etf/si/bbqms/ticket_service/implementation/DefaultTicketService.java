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
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;
import java.util.Set;

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

        final long currentHighestNumber = this.ticketRepository.findTopByServiceInAndBranchOrderByNumberDesc(possibleServices, branch)
                .map(Ticket::getNumber)
                .orElse(0L);

        final Ticket newTicket = new Ticket(currentHighestNumber + 1, Instant.now(), deviceToken, service, branch);
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

    @Override
    public List<Ticket> findAllByServicesAndBranch(final Set<Service> services, final Branch branch, final Sort sort) {
        return this.ticketRepository.findAllByServiceInAndBranch_Id(services, branch.getId(), sort).stream().toList();
    }
}
