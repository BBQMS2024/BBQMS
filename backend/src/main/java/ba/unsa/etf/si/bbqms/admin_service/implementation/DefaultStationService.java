package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.StationService;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Display;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.repository.DisplayRepository;
import ba.unsa.etf.si.bbqms.repository.ServiceRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@org.springframework.stereotype.Service
public class DefaultStationService implements StationService {
    private final TellerStationRepository tellerStationRepository;
    private final ServiceRepository serviceRepository;
    private final DisplayRepository displayRepository;

    public DefaultStationService(final TellerStationRepository tellerStationRepository,
                                 final ServiceRepository serviceRepository,
                                 final DisplayRepository displayRepository) {
        this.tellerStationRepository = tellerStationRepository;
        this.serviceRepository = serviceRepository;
        this.displayRepository = displayRepository;
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

    @Override
    public TellerStation addTellerStationDisplay(final long stationId, final long displayId) {
        final TellerStation tellerStation = this.tellerStationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("No station found with id: " + stationId ));

        final Display display = this.displayRepository.findById(displayId)
                .orElseThrow(() -> new EntityNotFoundException("No display with id: " + displayId));

        tellerStation.setDisplay(display);
        display.setTellerStation(tellerStation);
        this.displayRepository.save(display);
        return this.tellerStationRepository.save(tellerStation);
    }

    @Override
    public TellerStation deleteTellerStationDisplay(final long stationId, final long displayId) {
        final TellerStation tellerStation = this.tellerStationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("No station found with id: " + stationId ));

        if (tellerStation.getDisplay().getId() != displayId) {
            throw new RuntimeException("Display is not assigned to station: " + stationId);
        }

        final Display display = this.displayRepository.findById(displayId)
                .orElseThrow(() -> new EntityNotFoundException("No display with id: " + displayId));

        tellerStation.setDisplay(null);
        display.setTellerStation(null);
        this.displayRepository.save(display);
        return this.tellerStationRepository.save(tellerStation);
    }

    @Override
    public Set<Service> getServicesByAssigned(final long stationId, final boolean assigned) {
        final TellerStation tellerStation = this.tellerStationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("No station found with id: " + stationId ));

        if (assigned) {
            return tellerStation.getServices();
        }
        else {
            final Set<Service> assignedServices = tellerStation.getServices();
            final Set<Service> allServicesForGroup = new HashSet<>();
            final Set<BranchGroup> branchGroupSet = tellerStation.getBranch().getBranchGroups();

            for (BranchGroup branchGroup : branchGroupSet) {
                allServicesForGroup.addAll(branchGroup.getServices());
            }

            allServicesForGroup.removeAll(assignedServices);
            return allServicesForGroup;
        }
    }
}
