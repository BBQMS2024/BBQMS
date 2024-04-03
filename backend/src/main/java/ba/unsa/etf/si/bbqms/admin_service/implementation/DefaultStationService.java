package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.StationService;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.repository.ServiceRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;


@org.springframework.stereotype.Service
public class DefaultStationService implements StationService {
    private final TellerStationRepository tellerStationRepository;
    private final ServiceRepository serviceRepository;

    public DefaultStationService(final TellerStationRepository tellerStationRepository,
                                 final ServiceRepository serviceRepository) {
        this.tellerStationRepository = tellerStationRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<TellerStation> getAllByTenant(final String tenantCode) {
        return this.tellerStationRepository.findByBranch_Tenant_Code(tenantCode);
    }

    @Override
    public TellerStation addTellerStationService(final long stationId, final long serviceId) {
        final TellerStation tellerStation = this.tellerStationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("No station found with id: " + stationId ));
        final Service service = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + serviceId));

        tellerStation.getServices().add(service);

        return this.tellerStationRepository.save(tellerStation);
    }

    @Override
    public TellerStation deleteTellerStationService(final long stationId, final long serviceId) {
        final TellerStation tellerStation = this.tellerStationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("No station found with id: " + stationId ));
        final Service service = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + serviceId));

        if (tellerStation.getServices().remove(service)) {
            return this.tellerStationRepository.save(tellerStation);
        }
        throw new EntityNotFoundException("Teller station doesn't contain service with id: " + serviceId);
    }
}
