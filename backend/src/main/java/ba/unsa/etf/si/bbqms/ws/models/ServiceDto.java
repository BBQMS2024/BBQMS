package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Service;

public record ServiceDto(long id, String name) {
    public static ServiceDto fromEntity(final Service service) {
        return new ServiceDto(
                service.getId(),
                service.getName()
        );
    }
}
