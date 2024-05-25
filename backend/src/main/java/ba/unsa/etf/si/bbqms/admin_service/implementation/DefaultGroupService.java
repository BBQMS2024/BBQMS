package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.ekapic1.stonebase.filter.CompositeField;
import ba.ekapic1.stonebase.filter.ConditionType;
import ba.unsa.etf.si.bbqms.admin_service.api.StationService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.BranchField;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.admin_service.api.GroupService;
import ba.unsa.etf.si.bbqms.domain.ServiceField;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.domain.TenantField;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.repository.*;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupCreateDto;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class DefaultGroupService implements GroupService {
    private final StationService stationService;
    private final BranchGroupRepository branchGroupRepository;
    private final BranchRepository branchRepository;
    private final ServiceRepository serviceRepository;
    private final TenantRepository tenantRepository;
    private final TellerStationRepository stationRepository;
    private final TicketRepository ticketRepository;

    public DefaultGroupService(final StationService stationService,
                               final BranchGroupRepository branchGroupRepository,
                               final BranchRepository branchRepository,
                               final ServiceRepository serviceRepository,
                               final TenantRepository tenantRepository,
                               final TellerStationRepository stationRepository,
                               final TicketRepository ticketRepository) {
        this.stationService = stationService;
        this.branchGroupRepository = branchGroupRepository;
        this.branchRepository = branchRepository;
        this.serviceRepository = serviceRepository;
        this.tenantRepository = tenantRepository;
        this.stationRepository = stationRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public BranchGroup get(final long id) {
        return this.branchGroupRepository.get(id);
    }


    @Override
    public List<Branch> findAssignableBranches(final long groupId, final String tenantCode) {
        final List<Branch> tenantBranches = this.branchRepository.findAll(
                this.branchRepository.filterBuilder()
                        .with(CompositeField.of(BranchField.TENANT, TenantField.CODE), ConditionType.EQUAL, tenantCode)
                        .build()
        );

        final BranchGroup group = this.branchGroupRepository.get(groupId);

        tenantBranches.removeAll(group.getBranches());

        return tenantBranches;
    }

    @Override
    public List<Service> findAssignableServices(final long groupId, final String tenantCode) {
        final List<Service> tenantServices = this.serviceRepository.findAll(
                this.serviceRepository.filterBuilder()
                        .with(CompositeField.of(ServiceField.TENANT, TenantField.CODE), ConditionType.EQUAL, tenantCode)
                        .build()
        );

        final BranchGroup group = this.branchGroupRepository.get(groupId);

        tenantServices.removeAll(group.getServices());

        return tenantServices;
    }

    @Override
    public BranchGroup addBranchGroup(final String tenantCode, final BranchGroupCreateDto request) throws Exception {
        final Set<Service> services = new HashSet<>();
        final Set<Branch> branches = new HashSet<>();
        final Tenant tenant = this.tenantRepository.findByCode(tenantCode)
                .orElseThrow(() -> new EntityNotFoundException("No tenant with code " + tenantCode));

        if (request.serviceIds() != null) {
            for (final long serviceId : request.serviceIds()) {
                Service service = this.serviceRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + serviceId));
                services.add(service);
            }
        }

        if (request.branchIds() == null || request.branchIds().isEmpty()) {
            throw new Exception("Branch group must contain at least one branch");
        }

        for (final long branchId : request.branchIds()) {
            Branch branch = this.branchRepository.findById(branchId).orElseThrow(() -> new EntityNotFoundException("No service found with Id: " + branchId));
            branches.add(branch);
        }

        final BranchGroup branchGroup = new BranchGroup(request.name(), branches, services, tenant);
        return this.branchGroupRepository.save(branchGroup);
    }

    @Override
    public BranchGroup updateBranchGroup(final long branchGroupId, final BranchGroupUpdateDto request) throws EntityNotFoundException {
        final BranchGroup branchGroup = this.branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with Id: " + branchGroupId));

        if (request.name() != null && !request.name().trim().isEmpty()) {
            branchGroup.setName(request.name());
        }
        return this.branchGroupRepository.save(branchGroup);
    }

    @Override
    public BranchGroup addBranchGroupService(final long branchGroupId, final long serviceId) throws EntityNotFoundException {
        final BranchGroup branchGroup = this.branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with id: " + branchGroupId));
        final Service existingService = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + serviceId));

        branchGroup.getServices().add(existingService);

        return this.branchGroupRepository.save(branchGroup);
    }

    @Override
    public BranchGroup addBranchGroupBranch(final long branchGroupId, final long branchId) throws EntityNotFoundException {
        final BranchGroup branchGroup = this.branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with id: " + branchGroupId));
        final Branch existingBranch = this.branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("No branch found with id: " + branchId));

        branchGroup.getBranches().add(existingBranch);

        return this.branchGroupRepository.save(branchGroup);
    }

    @Override
    public List<BranchGroup> getAllByTenant(final String tenantCode) {
        return this.branchGroupRepository.findGroupsByTenantCode(tenantCode);
    }

    @Override
    public void deleteBranchGroup(final long branchGroupId) {
        final BranchGroup group = this.branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branchgroup with id: " + branchGroupId));


        final Set<Branch> branches = group.getBranches();
        for (final Branch existingBranch : branches) {
            final Set<Service> noLongerAvailable = discardUnavailableServicesFromStations(existingBranch, group);

            // We discard tickets which used the now removed services
            discardUnavailableTickets(noLongerAvailable, existingBranch.getId());

            this.branchRepository.save(existingBranch);
        }

        this.branchGroupRepository.deleteById(branchGroupId);
    }

    @Override
    public BranchGroup deleteBranchGroupService(final long branchGroupId, final long serviceId) throws EntityNotFoundException {
        final BranchGroup branchGroup = this.branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with id: " + branchGroupId));
        final Service existingService = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + serviceId));

        // If we are deleting a service from a group we also must remove it from stations in the group beforehand.
        for (final TellerStation station : this.stationService.getAllOfferingService(existingService)) {
            this.stationService.deleteTellerStationService(station.getId(), serviceId);
        }

        /*
            TODO: After removing a service from station, for each ticket that was affected, we should check
                  if there are any other stations in its corresponding branch that offer this service, and if none,
                  discard the ticket. This is easier said than done. Will be implemented if time allows.
                  For now, if this situation happens, the ticket will simply say it has no available stations,
                  but will not be automatically discarded.
         */

        if (branchGroup.getServices().remove(existingService)) {
            return this.branchGroupRepository.save(branchGroup);
        }
        throw new EntityNotFoundException("Group doesn't contain service with id: " + existingService.getId());
    }

    @Override
    @Transactional
    public BranchGroup deleteBranchGroupBranch(final long branchGroupId, final long branchId) throws EntityNotFoundException {
        final BranchGroup branchGroup = this.branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with id: " + branchGroupId));
        final Branch existingBranch = this.branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("No branch found with id: " + branchId));

        if (branchGroup.getBranches().remove(existingBranch)) {
            final Set<Service> noLongerAvailable = discardUnavailableServicesFromStations(existingBranch, branchGroup);

            // We discard tickets which used the now removed services
            discardUnavailableTickets(noLongerAvailable, branchId);

            this.branchRepository.save(existingBranch);
            return this.branchGroupRepository.save(branchGroup);
        }
        throw new EntityNotFoundException("Group doesn't contain branch with id: " + existingBranch.getId());
    }

    @Override
    public Set<BranchGroup> getAllOfferingService(final Service service) {
        return this.branchGroupRepository.findAllByServicesContains(service);
    }

    private Set<Service> discardUnavailableServicesFromStations(final Branch branch, final BranchGroup removedGroup) {
        branch.getBranchGroups().remove(removedGroup);
        final Set<Service> noLongerAvailableServices = new HashSet<>(removedGroup.getServices());

        // We removed the branch group reference from branch so what's left is other groups and their services.
        // Essentially we take all services which this group offered, and remove those which are offered elsewhere.
        // Remaining are those which are no longer available
        for (final BranchGroup group : branch.getBranchGroups()) {
            noLongerAvailableServices.removeAll(group.getServices());
        }

        for (final TellerStation station : branch.getTellerStations()) {
            station.getServices().removeAll(noLongerAvailableServices);
        }

        this.stationRepository.saveAll(branch.getTellerStations());

        return noLongerAvailableServices;
    }

    private void discardUnavailableTickets(final Set<Service> noLongerAvailableServices, final long branchId) {
        final Set<Long> ticketsToBeDiscarded = this.ticketRepository.findAllByServiceInAndBranch_Id(noLongerAvailableServices, branchId).stream()
                .map(Ticket::getId)
                .collect(Collectors.toSet());
        this.ticketRepository.deleteAllById(ticketsToBeDiscarded);
    }
}
