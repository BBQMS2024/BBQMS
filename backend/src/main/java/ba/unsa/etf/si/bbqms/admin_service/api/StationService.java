package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StationService {
    List<TellerStation> getAllByTenant(final String tenantCode);
    Set<Service> findAssignableServices(final long stationId);
    TellerStation addTellerStationService(final long stationId, final long serviceId);
    TellerStation deleteTellerStationService(final long stationId, final long serviceId);
    TellerStation addTellerStationDisplay(final long stationId, final long displayId);
    TellerStation deleteTellerStationDisplay(final long stationId, final long displayId);
    Set<Service> getServicesByAssigned(final long stationId, final boolean assigned);
    List<TellerStation> getAllByBranch(final long branchId);
    Set<TellerStation> getAllOfferingService(final Service service);
    Optional<TellerStation> findById(final long serviceId);
}
