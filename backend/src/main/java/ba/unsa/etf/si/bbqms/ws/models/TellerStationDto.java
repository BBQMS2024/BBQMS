package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.TellerStation;

public record TellerStationDto(long id, String name) {
    public static TellerStationDto fromEntity(final TellerStation tellerStation) {
        return new TellerStationDto(
                tellerStation.getId(),
                tellerStation.getName()
        );
    }
}