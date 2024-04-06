package ba.unsa.etf.si.bbqms.ws.models;

import java.util.Set;

public record BranchGroupCreateDto(String name, Set<Long> serviceIds, Set<Long> branchIds) {
}
