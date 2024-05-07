package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.admin_service.api.GroupService;
import ba.unsa.etf.si.bbqms.domain.*;
import ba.unsa.etf.si.bbqms.repository.BranchRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import ba.unsa.etf.si.bbqms.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;


import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class DefaultBranchService implements BranchService {
    private final BranchRepository branchRepository;
    private final TenantRepository tenantRepository;
    private final TellerStationRepository tellerStationRepository;
    private final TicketRepository ticketRepository;
    private final GroupService groupService;

    public DefaultBranchService(final BranchRepository branchRepository,
                                final TenantRepository tenantRepository,
                                final TellerStationRepository tellerStationRepository,
                                final TicketRepository ticketRepository,
                                final GroupService groupService) {
        this.branchRepository = branchRepository;
        this.tenantRepository = tenantRepository;
        this.tellerStationRepository = tellerStationRepository;
        this.ticketRepository = ticketRepository;
        this.groupService = groupService;
    }

    @Override
    public Optional<Branch> findById(final long branchId) {
        return this.branchRepository.findById(branchId);
    }

    @Override
    public Branch createBranch(final String name, final List<String> tellerStations, final String tenantCode) throws Exception {
        final Tenant userTenant = this.tenantRepository.findByCode(tenantCode)
                .orElseThrow(() -> new Exception("Tenant with code: " + tenantCode + " doesn't exist."));

        final Branch newBranch = new Branch(name, userTenant);

        final Set<TellerStation> newTellerStations;
        if (tellerStations != null) {
            newTellerStations = tellerStations.stream()
                    .map(stationName -> new TellerStation(stationName, newBranch))
                    .collect(Collectors.toSet());
        } else {
            newTellerStations = null;
        }
        newBranch.setTellerStations(newTellerStations);

        return this.branchRepository.save(newBranch);
    }

    @Override
    public Set<Branch> getBranches(final String tenantCode) {
        return this.branchRepository.findAllByTenant_Code(tenantCode);
    }

    @Override
    public Branch updateBranch(final long branchId,
                               final String name,
                               final List<String> newTellerStations,
                               final String tenantCode) throws Exception {

        final Branch existingBranch = this.branchRepository.findById(branchId)
                .orElseThrow(() -> new Exception("Branch with id: " + branchId + " not found."));

        existingBranch.setName(name);

        if (newTellerStations != null) {
            if (existingBranch.getTellerStations() != null) {
                existingBranch.getTellerStations().clear();
            }

            final Set<TellerStation> tellerStations = newTellerStations.stream()
                    .map(stationName -> new TellerStation(stationName, existingBranch))
                    .collect(Collectors.toSet());
            existingBranch.getTellerStations().addAll(tellerStations);
        }

        return this.branchRepository.save(existingBranch);
    }

    @Override
    public void removeBranch(final long branchId) {
        final Branch branch = this.branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch with id: " + branchId + " doesn't exist."));

        // Discard all tickets which are at the current branch
        final Set<Long> ticketsToBeDiscarded = this.ticketRepository.findAllByBranch_Id(branch.getId()).stream()
                .map(Ticket::getId)
                .collect(Collectors.toSet());
        this.ticketRepository.deleteAllById(ticketsToBeDiscarded);

        for (final BranchGroup group : branch.getBranchGroups()) {
            // We have do this to prevent FK constraint popping due to many-to-many
            this.groupService.deleteBranchGroupBranch(group.getId(), branch.getId());
        }

        this.branchRepository.deleteById(branchId);
    }

    @Override
    public TellerStation addStation(final String name, final long branchId) throws Exception {
        final Branch branch = this.branchRepository.findById(branchId)
                .orElseThrow(() -> new Exception("Branch with id: " + branchId + " not found."));

        branch.getTellerStations().add(new TellerStation(name, branch));

        final Branch savedBranch = this.branchRepository.save(branch);
        return savedBranch.getTellerStations().stream()
                .filter(station -> station.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new Exception("Did not add the station properly."));
    }

    /*
        TODO: If station is removed, and some ticket is currently on it?
               Also, if station is removed, and some tickets service is only available at that station,
               it should be discarded.
     */
    @Override
    public void removeStation(final long stationId) throws Exception {
        this.tellerStationRepository.deleteById(stationId);
    }

    @Override
    public TellerStation updateStation(final long stationId, final String name) throws Exception {
        final TellerStation existingStation = this.tellerStationRepository.findById(stationId)
                .orElseThrow(() -> new Exception("No station with id: " + stationId));

        existingStation.setName(name);
        return this.tellerStationRepository.save(existingStation);
    }

    @Override
    public Set<Service> extractPossibleServices(final Branch branch) {
        final Set<BranchGroup> groups = branch.getBranchGroups();
        final Set<Service> possibleServices = new TreeSet<>(Comparator.comparing(Service::getId));
        for (final BranchGroup group : groups) {
            possibleServices.addAll(group.getServices());
        }
        return possibleServices;
    }

    @Override
    public Set<TellerStation> getStationsWithService(final Branch branch, final Service service) {
        return branch.getTellerStations().stream()
                .filter(station -> station.getServices().contains(service))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Ticket> getTicketsWithService(final long branchId, final long serviceId) {
        return this.ticketRepository.findAllByService_IdAndBranch_Id(serviceId,branchId);
    }

    @Override
    public boolean isInSpecifiedTenant(final long branchId, final String tenantCode) {
        final Optional<Branch> branch = this.branchRepository.findById(branchId);
        return branch.get().getTenant().getCode().equals(tenantCode);
    }

    @Override
    public boolean isInSpecifiedTenant(final long branchId, final long stationId, final String tenantCode) {
        final Optional<Branch> branch = this.branchRepository.findById(branchId);
        final Optional<TellerStation> station = this.tellerStationRepository.findById(stationId);
        return branch.get().getTenant().getCode().equals(tenantCode) && station.get().getBranch().equals(branch.get());
    }
}
