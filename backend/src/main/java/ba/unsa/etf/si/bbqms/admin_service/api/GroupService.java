package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupCreateDto;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupUpdateDto;

import java.util.List;

public interface GroupService {
    BranchGroup addBranchGroup(final BranchGroupCreateDto name);
    BranchGroup updateBranchGroup(final Long branchGroupId, final BranchGroupUpdateDto request);
    BranchGroup addBranchGroupService(final Long branchGroupId, final Long serviceId);
    BranchGroup addBranchGroupBranch(final Long branchGroupId, final Long branchId);
    BranchGroup deleteBranchGroupBranch(final Long branchGroupId, final Long branchId);
    BranchGroup deleteBranchGroupService(final Long branchGroupId, final Long serviceId);
    List<BranchGroup> getAll();
    void deleteBranchGroup(final Long branchGroupId);
}
