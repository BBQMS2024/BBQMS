package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupCreateDto;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupUpdateDto;

import java.util.List;
import java.util.Set;

public interface GroupService {
    BranchGroup get(final long id);
    List<Branch> findAssignableBranches(final long groupId, final String tenantCode);
    List<Service> findAssignableServices(final long groupId, final String tenantCode);
    BranchGroup addBranchGroup(final String tenantCode, final BranchGroupCreateDto name) throws Exception;
    BranchGroup updateBranchGroup(final long branchGroupId, final BranchGroupUpdateDto request);
    BranchGroup addBranchGroupService(final long branchGroupId, final long serviceId);
    BranchGroup addBranchGroupBranch(final long branchGroupId, final long branchId);
    BranchGroup deleteBranchGroupBranch(final long branchGroupId, final long branchId);
    BranchGroup deleteBranchGroupService(final long branchGroupId, final long serviceId);
    List<BranchGroup> getAllByTenant(final String tenantCode);
    void deleteBranchGroup(final long branchGroupId);
    Set<BranchGroup> getAllOfferingService(final Service service);
}
