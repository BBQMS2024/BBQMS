package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Branch;

public record BranchDto(long id, String name) {
    public static BranchDto fromEntity(final Branch branch) {
        return new BranchDto(
                branch.getId(),
                branch.getName()
        );
    }
}
