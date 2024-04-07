package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.repository.BranchRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class DefaultBranchService implements BranchService {
    private final BranchRepository branchRepository;
    private final TenantRepository tenantRepository;
    private final TellerStationRepository tellerStationRepository;

    public DefaultBranchService(final BranchRepository branchRepository,
                                final TenantRepository tenantRepository,
                                final TellerStationRepository tellerStationRepository) {
        this.branchRepository = branchRepository;
        this.tenantRepository = tenantRepository;
        this.tellerStationRepository = tellerStationRepository;
    }

    @Override
    public Optional<Branch> findById(final long branchId) {
        return this.branchRepository.findById(branchId);
    }

    @Override
    public Branch createBranch(final String name, final List<String> tellerStations, final String tenantCode) throws Exception {
        final Tenant userTenant = this.tenantRepository.findByCode(tenantCode)
                .orElseThrow(() -> new Exception("Tenant with code: " + tenantCode + " doesn't exist."));

        final Branch newBranch = new Branch(name, userTenant);

        final Set<TellerStation> newTellerStations;
        if (tellerStations != null) {
            newTellerStations = tellerStations.stream()
                    .map(stationName -> new TellerStation(stationName, newBranch))
                    .collect(Collectors.toSet());
        } else {
            newTellerStations = null;
        }
        newBranch.setTellerStations(newTellerStations);

        return this.branchRepository.save(newBranch);
    }

    @Override
    public Set<Branch> getBranches(final String tenantCode) {
        return this.branchRepository.findAllByTenant_Code(tenantCode);
    }

    @Override
    public Branch updateBranch(final long branchId,
                               final String name,
                               final List<String> newTellerStations,
                               final String tenantCode) throws Exception {

        final Branch existingBranch = this.branchRepository.findById(branchId)
                .orElseThrow(() -> new Exception("Branch with id: " + branchId + " not found."));

        existingBranch.setName(name);

        if (newTellerStations != null) {
            if (existingBranch.getTellerStations() != null) {
                existingBranch.getTellerStations().clear();
            }

            final Set<TellerStation> tellerStations = newTellerStations.stream()
                    .map(stationName -> new TellerStation(stationName, existingBranch))
                    .collect(Collectors.toSet());
            existingBranch.getTellerStations().addAll(tellerStations);
        }


        return this.branchRepository.save(existingBranch);
    }

    @Override
    public void removeBranch(final long branchId) {
        this.branchRepository.deleteById(branchId);
    }

    @Override
    public TellerStation addStation(final String name, final long branchId) throws Exception {
        final Branch branch = this.branchRepository.findById(branchId)
                .orElseThrow(() -> new Exception("Branch with id: " + branchId + " not found."));

        branch.getTellerStations().add(new TellerStation(name, branch));

        final Branch savedBranch = this.branchRepository.save(branch);
        return savedBranch.getTellerStations().stream()
                .filter(station -> station.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new Exception("Did not add the station properly."));
    }

    @Override
    public void removeStation(final long stationId) throws Exception {
        this.tellerStationRepository.deleteById(stationId);
    }

    @Override
    public TellerStation updateStation(final long stationId, final String name) throws Exception {
        final TellerStation existingStation = this.tellerStationRepository.findById(stationId)
                .orElseThrow(() -> new Exception("No station with id: " + stationId));

        existingStation.setName(name);
        return this.tellerStationRepository.save(existingStation);
    }

    @Override
    public Set<Service> extractPossibleServices(final Branch branch) {
        final Set<BranchGroup> groups = branch.getBranchGroups();
        final Set<Service> possibleServices = new HashSet<>();
        for (final BranchGroup group : groups) {
            possibleServices.addAll(group.getServices());
        }
        return possibleServices;
    }
}
