package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.Tenant;

public record ServiceDto(long id, String name, Tenant tenant) {
    public static ServiceDto fromEntity(final Service service) {
        return new ServiceDto(
                service.getId(),
                service.getName(),
                service.getTenant()
        );
    }
}
