package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Display;

public record DisplayDto(long id, String name, TellerStationDto tellerStation, BranchDto branch) {
    public static DisplayDto fromEntity(final Display display) {
        return new DisplayDto(
                display.getId(),
                display.getName(),
                TellerStationDto.fromEntity(display.getTellerStation()),
                BranchDto.fromEntity(display.getBranch())
        );
    }
}
