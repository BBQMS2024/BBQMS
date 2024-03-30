package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Branch;

import java.util.Set;
import java.util.stream.Collectors;

public record BranchDto(long id, String name, Set<TellerStationDto> tellerStations) {
    public static BranchDto fromEntity(final Branch branch) {
        final Set<TellerStationDto> stations;
        if (branch.getTellerStations() != null) {
            stations = branch.getTellerStations().stream().map(TellerStationDto::fromEntity).collect(Collectors.toSet());
        } else {
            stations = null;
        }
        return new BranchDto(
                branch.getId(),
                branch.getName(),
                stations
        );
    }
}
