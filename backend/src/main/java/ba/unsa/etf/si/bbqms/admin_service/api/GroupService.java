package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupCreateDto;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupUpdateDto;

import java.util.List;

public interface GroupService {
    BranchGroup addBranchGroup(final BranchGroupCreateDto name) throws Exception;
    BranchGroup updateBranchGroup(final long branchGroupId, final BranchGroupUpdateDto request);
    BranchGroup addBranchGroupService(final long branchGroupId, final long serviceId);
    BranchGroup addBranchGroupBranch(final long branchGroupId, final long branchId);
    BranchGroup deleteBranchGroupBranch(final long branchGroupId, final long branchId);
    BranchGroup deleteBranchGroupService(final long branchGroupId, final long serviceId);
    List<BranchGroup> getAllByTenant(final String tenantCode);
    void deleteBranchGroup(final long branchGroupId);
}
