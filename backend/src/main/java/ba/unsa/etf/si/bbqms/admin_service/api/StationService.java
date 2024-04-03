package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.TellerStation;

import java.util.List;

public interface StationService {
    List<TellerStation> getAllByTenant(final String tenantCode);

    TellerStation addTellerStationService(final long stationId, final long serviceId);

    TellerStation deleteTellerStationService(final long stationId, final long serviceId);

    TellerStation addTellerStationDisplay(final long stationId, final long displayId);

    TellerStation deleteTellerStationDisplay(final long stationId, final long displayId);
}
