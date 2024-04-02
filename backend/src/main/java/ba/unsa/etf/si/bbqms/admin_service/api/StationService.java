package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.TellerStation;

public interface StationService {
    TellerStation addTellerStationService(final long stationId, final long serviceId);

    TellerStation deleteTellerStationService(final long stationId, final long serviceId);
}
