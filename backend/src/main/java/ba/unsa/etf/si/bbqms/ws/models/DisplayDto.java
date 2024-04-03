package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Display;

public record DisplayDto(long id, String name, TellerStationDto tellerStation, BranchDto branch) {
    public static DisplayDto fromEntity(final Display display) {
        final TellerStationDto tellerStationDto;
        if (display.getTellerStation() == null) {
            tellerStationDto = null;
        }
        else {
            tellerStationDto = TellerStationDto.fromEntity(display.getTellerStation());
        }
        return new DisplayDto(
                display.getId(),
                display.getName(),
                tellerStationDto,
                BranchDto.fromEntity(display.getBranch())
        );
    }
}
