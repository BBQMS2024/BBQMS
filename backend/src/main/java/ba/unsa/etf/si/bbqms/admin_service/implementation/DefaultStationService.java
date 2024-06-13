package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.ekapic1.stonebase.filter.CompositeField;
import ba.ekapic1.stonebase.filter.ConditionType;
import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.admin_service.api.StationService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.BranchField;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Display;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.TellerStationField;
import ba.unsa.etf.si.bbqms.domain.TenantField;
import ba.unsa.etf.si.bbqms.repository.DisplayRepository;
import ba.unsa.etf.si.bbqms.repository.ServiceRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;


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
        return this.tellerStationRepository.findAll(
                this.tellerStationRepository.filterBuilder()
                        .with(
                                CompositeField.of(
                                        TellerStationField.BRANCH,
                                        BranchField.TENANT,
                                        TenantField.CODE
                                ),
                                ConditionType.EQUAL,
                                tenantCode
                        )
                        .build()
        );
    }

    @Override
    public Set<Service> findAssignableServices(final long stationId) {
        final TellerStation station = this.tellerStationRepository.get(stationId);

        final Set<BranchGroup> groups = station.getBranch().getBranchGroups();
        final Set<Service> possibleServices = new TreeSet<>(Comparator.comparing(Service::getId));
        for (final BranchGroup group : groups) {
            possibleServices.addAll(group.getServices());
        }

        possibleServices.removeAll(station.getServices());

        return possibleServices;
    }

    @Override
    public TellerStation addTellerStationService(final long stationId, final long serviceId) {
        final TellerStation tellerStation = this.tellerStationRepository.get(stationId);
        final Service service = this.serviceRepository.get(serviceId);

        tellerStation.getServices().add(service);

        return this.tellerStationRepository.save(tellerStation);
    }

    @Override
    public TellerStation deleteTellerStationService(final long stationId, final long serviceId) {
        final TellerStation tellerStation = this.tellerStationRepository.get(stationId);
        final Service service = this.serviceRepository.get(serviceId);

        if (tellerStation.getServices().remove(service)) {
            return this.tellerStationRepository.save(tellerStation);
        }
        throw new EntityNotFoundException("Teller station doesn't contain service with id: " + serviceId);
    }

    @Override
    public TellerStation addTellerStationDisplay(final long stationId, final long displayId) {
        final TellerStation tellerStation = this.tellerStationRepository.get(stationId);
        final Display display = this.displayRepository.get(displayId);

        tellerStation.setDisplay(display);
        display.setTellerStation(tellerStation);
        this.displayRepository.save(display);
        return this.tellerStationRepository.save(tellerStation);
    }

    @Override
    public TellerStation deleteTellerStationDisplay(final long stationId, final long displayId) {
        final TellerStation tellerStation = this.tellerStationRepository.get(stationId);

        if (tellerStation.getDisplay().getId() != displayId) {
            throw new RuntimeException("Display is not assigned to station: " + stationId);
        }

        final Display display = this.displayRepository.get(displayId);

        tellerStation.setDisplay(null);
        display.setTellerStation(null);
        this.displayRepository.save(display);
        return this.tellerStationRepository.save(tellerStation);
    }

    @Override
    public Set<Service> getServicesByAssigned(final long stationId, final boolean assigned) {
        final TellerStation tellerStation = this.tellerStationRepository.get(stationId);

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

    @Override
    public List<TellerStation> getAllByBranch(final long branchId) {
        return this.tellerStationRepository.findAll(
                this.tellerStationRepository.filterBuilder()
                        .with(CompositeField.of(TellerStationField.BRANCH, BranchField.ID), ConditionType.EQUAL, branchId)
                        .build()
        );
    }

    @Override
    public Set<TellerStation> getAllOfferingService(final Service service) {
        return this.tellerStationRepository.findAllByServicesContains(service);
    }

    @Override
    public Optional<TellerStation> findById(final long serviceId) {
        return this.tellerStationRepository.findById(serviceId);
    }
}
