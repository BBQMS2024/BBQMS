package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.BranchGroup;
<<<<<<< HEAD
=======
import ba.unsa.etf.si.bbqms.domain.Service;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupCreateDto;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupUpdateDto;

import java.util.List;
<<<<<<< HEAD

public interface GroupService {
    BranchGroup addBranchGroup(final BranchGroupCreateDto name) throws Exception;
=======
import java.util.Set;

public interface GroupService {
    BranchGroup addBranchGroup(final String tenantCode, final BranchGroupCreateDto name) throws Exception;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
    BranchGroup updateBranchGroup(final long branchGroupId, final BranchGroupUpdateDto request);
    BranchGroup addBranchGroupService(final long branchGroupId, final long serviceId);
    BranchGroup addBranchGroupBranch(final long branchGroupId, final long branchId);
    BranchGroup deleteBranchGroupBranch(final long branchGroupId, final long branchId);
    BranchGroup deleteBranchGroupService(final long branchGroupId, final long serviceId);
    List<BranchGroup> getAllByTenant(final String tenantCode);
    void deleteBranchGroup(final long branchGroupId);
<<<<<<< HEAD
=======
    Set<BranchGroup> getAllOfferingService(final Service service);
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
}
