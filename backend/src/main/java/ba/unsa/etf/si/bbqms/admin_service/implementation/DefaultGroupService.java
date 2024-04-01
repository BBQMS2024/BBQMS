package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.admin_service.api.GroupService;
import ba.unsa.etf.si.bbqms.repository.BranchGroupRepository;
import ba.unsa.etf.si.bbqms.repository.BranchRepository;
import ba.unsa.etf.si.bbqms.repository.ServiceRepository;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupCreateDto;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupUpdateDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
public class DefaultGroupService implements GroupService {
    private final BranchGroupRepository branchGroupRepository;
    private final BranchRepository branchRepository;
    private final ServiceRepository serviceRepository;

    public DefaultGroupService(final BranchGroupRepository branchGroupRepository,
                               final BranchRepository branchRepository,
                               final ServiceRepository serviceRepository) {
        this.branchGroupRepository = branchGroupRepository;
        this.branchRepository = branchRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public BranchGroup addBranchGroup(final BranchGroupCreateDto request) throws EntityNotFoundException{
        Set<Service> services = new HashSet<>();
        Set<Branch> branches = new HashSet<>();

        if (request.serviceIds() != null) {
            for (Long serviceId : request.serviceIds()) {
                Service service = serviceRepository.findById(serviceId).orElseThrow(() -> new EntityNotFoundException("No service found with Id: " + serviceId));
                services.add(service);
            }
        }
        if (request.branchIds() != null) {
            for (Long branchId : request.branchIds()) {
                Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new EntityNotFoundException("No service found with Id: " + branchId));
                branches.add(branch);
            }
        }

        BranchGroup branchGroup = new BranchGroup(request.name(),branches,services);
        return branchGroupRepository.save(branchGroup);
    }

    @Override
    public BranchGroup updateBranchGroup(final Long branchGroupId, final BranchGroupUpdateDto request) throws EntityNotFoundException{
        final BranchGroup branchGroup = branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with Id: " + branchGroupId));

        if (request.name() != null && !request.name().trim().isEmpty()) {
            branchGroup.setName(request.name());
        }
        return branchGroupRepository.save(branchGroup);
    }

    @Override
    public BranchGroup addBranchGroupService(final Long branchGroupId, final Long serviceId) throws EntityNotFoundException{
        final BranchGroup branchGroup = branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with id: " + branchGroupId));
        final Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + serviceId));

        branchGroup.getServices().add(existingService);

        return branchGroupRepository.save(branchGroup);
    }

    @Override
    public BranchGroup addBranchGroupBranch(final Long branchGroupId, final Long branchId) throws EntityNotFoundException{
        final BranchGroup branchGroup = branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with id: " + branchGroupId));
        final Branch existingBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("No branch found with id: " + branchId));

        branchGroup.getBranches().add(existingBranch);

        return branchGroupRepository.save(branchGroup);
    }

    @Override
    public List<BranchGroup> getAll() {
        return branchGroupRepository.findAll();
    }

    @Override
    public void deleteBranchGroup(final Long branchGroupId) throws EntityNotFoundException{
        branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with id: " + branchGroupId));

        branchGroupRepository.deleteById(branchGroupId);
    }

    @Override
    public BranchGroup deleteBranchGroupService(final Long branchGroupId, final Long serviceId) throws EntityNotFoundException{
        final BranchGroup branchGroup = branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with id: " + branchGroupId));
        final Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + serviceId));

        if (branchGroup.getServices().remove(existingService)) {
            return branchGroupRepository.save(branchGroup);
        }
        throw new EntityNotFoundException("Group doesn't contain service with id: " + existingService.getId());
    }

    @Override
    public BranchGroup deleteBranchGroupBranch(final Long branchGroupId, final Long branchId) throws EntityNotFoundException{
        final BranchGroup branchGroup = branchGroupRepository.findById(branchGroupId)
                .orElseThrow(() -> new EntityNotFoundException("No branch group found with id: " + branchGroupId));
        final Branch existingBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("No branch found with id: " + branchId));

        if (branchGroup.getBranches().remove(existingBranch)) {
            return branchGroupRepository.save(branchGroup);
        }
        throw new EntityNotFoundException("Group doesn't contain branch with id: " + existingBranch.getId());
    }
}
