package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.StationService;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.repository.ServiceRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import jakarta.persistence.EntityNotFoundException;


@org.springframework.stereotype.Service
public class DefaultStationService implements StationService {
    private final TellerStationRepository tellerStationRepository;
    private final ServiceRepository serviceRepository;

    public DefaultStationService(TellerStationRepository tellerStationRepository,
                                 ServiceRepository serviceRepository) {
        this.tellerStationRepository = tellerStationRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public TellerStation addTellerStationService(final Long stationId, final Long serviceId) {
        final TellerStation tellerStation = tellerStationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("No station found with id: " + stationId ));
        final Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + serviceId));

        tellerStation.getServices().add(service);

        return tellerStationRepository.save(tellerStation);
    }

    @Override
    public TellerStation deleteTellerStationService(Long stationId, Long serviceId) {
        final TellerStation tellerStation = tellerStationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("No station found with id: " + stationId ));
        final Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + serviceId));

        if (tellerStation.getServices().remove(service)) {
            return tellerStationRepository.save(tellerStation);
        }
        throw new EntityNotFoundException("Teller station doesn't contain service with id: " + serviceId);
    }
}
