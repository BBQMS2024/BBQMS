package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.StationService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.admin_service.api.GroupService;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.repository.*;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupCreateDto;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupUpdateDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
public class DefaultGroupService implements GroupService {
    private final StationService stationService;
    private final BranchGroupRepository branchGroupRepository;
    private final BranchRepository branchRepository;
    private final ServiceRepository serviceRepository;
    private final TenantRepository tenantRepository;
    private final TellerStationRepository stationRepository;

    public DefaultGroupService(final StationService stationService,
                               final BranchGroupRepository branchGroupRepository,
                               final BranchRepository branchRepository,
                               final ServiceRepository serviceRepository,
                               final TenantRepository tenantRepository,
                               final TellerStationRepository stationRepository) {
        this.stationService = stationService;
        this.branchGroupRepository = branchGroupRepository;
        this.branchRepository = branchRepository;
        this.serviceRepository = serviceRepository;
        this.tenantRepository = tenantRepository;
        this.stationRepository = stationRepository;
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

        final BranchGroup branchGroup = new BranchGroup(request.name(),branches,services,tenant);
        return this.branchGroupRepository.save(branchGroup);
    }

    @Override
    public BranchGroup updateBranchGroup(final long branchGroupId, final BranchGroupUpdateDto request) throws EntityNotFoundException{
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
        for (final Branch existingBranch: branches) {
            existingBranch.getBranchGroups().remove(group);
            final Set<Service> noLongerAvailableServices = new HashSet<>(group.getServices());
            for (final BranchGroup existingGroups : existingBranch.getBranchGroups()) {
                noLongerAvailableServices.removeAll(existingGroups.getServices());
            }

            for (final TellerStation station : existingBranch.getTellerStations()) {
                station.getServices().removeAll(noLongerAvailableServices);
            }
            this.stationRepository.saveAll(existingBranch.getTellerStations());
            this.branchRepository.save(existingBranch);
        }

        this.branchGroupRepository.deleteById(branchGroupId);
    }

    @Override
    public BranchGroup deleteBranchGroupService(final long branchGroupId, final long serviceId) throws EntityNotFoundException{
        final BranchGroup branchGroup = this.branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with id: " + branchGroupId));
        final Service existingService = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + serviceId));

        // If we are deleting a service from a group we also must remove it from stations in the group beforehand. Same below.
        for (final TellerStation station : this.stationService.getAllOfferingService(existingService)) {
            this.stationService.deleteTellerStationService(station.getId(), serviceId);
        }

        if (branchGroup.getServices().remove(existingService)) {
            return this.branchGroupRepository.save(branchGroup);
        }
        throw new EntityNotFoundException("Group doesn't contain service with id: " + existingService.getId());
    }

    @Override
    public BranchGroup deleteBranchGroupBranch(final long branchGroupId, final long branchId) throws EntityNotFoundException{
        final BranchGroup branchGroup = this.branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with id: " + branchGroupId));
        final Branch existingBranch = this.branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("No branch found with id: " + branchId));

        if (branchGroup.getBranches().remove(existingBranch)) {
            existingBranch.getBranchGroups().remove(branchGroup);
            final Set<Service> noLongerAvailableServices = new HashSet<>(branchGroup.getServices());
            for (final BranchGroup group : existingBranch.getBranchGroups()) {
                noLongerAvailableServices.removeAll(group.getServices());
            }

            for (final TellerStation station : existingBranch.getTellerStations()) {
                station.getServices().removeAll(noLongerAvailableServices);
            }
            this.stationRepository.saveAll(existingBranch.getTellerStations());
            this.branchRepository.save(existingBranch);
            return this.branchGroupRepository.save(branchGroup);
        }
        throw new EntityNotFoundException("Group doesn't contain branch with id: " + existingBranch.getId());
    }

    @Override
    public Set<BranchGroup> getAllOfferingService(final Service service) {
        return this.branchGroupRepository.findAllByServicesContains(service);
    }
}
