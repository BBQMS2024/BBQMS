package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.Tenant;

public record ServiceResponseDto(long id, String name, Tenant tenant) {
    public static ServiceResponseDto fromEntity(final Service service) {
        return new ServiceResponseDto(
                service.getId(),
                service.getName(),
                service.getTenant()
        );
    }
}
