package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.TellerStation;

public interface StationService {
    TellerStation addTellerStationService(Long stationId, Long serviceId);

    TellerStation deleteTellerStationService(Long stationId, Long serviceId);
}
