package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;

import java.util.List;
<<<<<<< HEAD
=======
import java.util.Optional;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
import java.util.Set;

public interface StationService {
    List<TellerStation> getAllByTenant(final String tenantCode);
    TellerStation addTellerStationService(final long stationId, final long serviceId);
    TellerStation deleteTellerStationService(final long stationId, final long serviceId);
    TellerStation addTellerStationDisplay(final long stationId, final long displayId);
    TellerStation deleteTellerStationDisplay(final long stationId, final long displayId);
    Set<Service> getServicesByAssigned(final long stationId, final boolean assigned);
    Set<TellerStation> getAllByBranch(final long branchId);
<<<<<<< HEAD
=======
    Set<TellerStation> getAllOfferingService(final Service service);
    Optional<TellerStation> findById(final long serviceId);
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
}
