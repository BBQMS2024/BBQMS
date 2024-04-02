package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Service;

import java.util.Set;
import java.util.stream.Collectors;

public record BranchGroupResponseDto (Long id, String name, Set<ServiceResponseDto> services, Set<BranchDto> branches) {
    public static BranchGroupResponseDto fromEntity(final BranchGroup branchGroup) {
        Set<Branch> branchSet = branchGroup.getBranches();
        Set<Service> serviceSet = branchGroup.getServices();
        return new BranchGroupResponseDto(
                branchGroup.getId(),
                branchGroup.getName(),
                serviceSet.stream().map(ServiceResponseDto::fromEntity).collect(Collectors.toSet()),
                branchSet.stream().map(BranchDto::fromEntity).collect(Collectors.toSet())
        );
    }
}
