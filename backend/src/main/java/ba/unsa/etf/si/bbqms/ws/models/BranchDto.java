package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Tenant;

public record BranchDto(long id, String name, Tenant tenant) {
    public static BranchDto fromEntity(final Branch branch) {
        return new BranchDto(
                branch.getId(),
                branch.getName(),
                branch.getTenant()
        );
    }
}
