package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;

import java.util.Set;
import java.util.stream.Collectors;

public record TellerStationResponseDto(long id, String name, Set<ServiceDto> services) {
    public static TellerStationResponseDto fromEntity(final TellerStation tellerStation) {
        final Set<Service> serviceSet = tellerStation.getServices();
        return new TellerStationResponseDto(
                tellerStation.getId(),
                tellerStation.getName(),
                serviceSet.stream().map(ServiceDto::fromEntity).collect(Collectors.toSet())
        );
    }

}
