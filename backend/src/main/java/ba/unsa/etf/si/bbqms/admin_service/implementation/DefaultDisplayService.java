package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.DisplayService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Display;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.repository.BranchRepository;
import ba.unsa.etf.si.bbqms.repository.DisplayRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
<<<<<<< HEAD
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
=======
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
import java.util.Set;

@Service
public class DefaultDisplayService implements DisplayService {
    private final DisplayRepository displayRepository;
    private final TellerStationRepository tellerStationRepository;
    private final BranchRepository branchRepository;

    public DefaultDisplayService(final DisplayRepository displayRepository,
                                 final TellerStationRepository tellerStationRepository,
                                 final BranchRepository branchRepository) {
        this.displayRepository = displayRepository;
        this.tellerStationRepository = tellerStationRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public Display createDisplay(final String name, final long branchId) throws Exception {
        final Branch branch = this.branchRepository.findById(branchId)
                .orElseThrow(() -> new Exception("Branch with id: " + branchId + " doesn't exist."));

<<<<<<< HEAD
        final Display newDisplay = new Display(name, null, branch);
=======
        final Display newDisplay = new Display(name, branch);
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74

        return this.displayRepository.save(newDisplay);
    }

    @Override
    public Set<Display> getDisplays(final long branchId) throws Exception {
        this.branchRepository.findById(branchId)
                .orElseThrow(() -> new Exception("Branch with id: " + branchId + " doesn't exist."));

        return this.displayRepository.findByBranchId(branchId);
    }

    @Override
<<<<<<< HEAD
    public Set<Display> getDisplaysByTenant(String tenantCode) {
=======
    public Set<Display> getDisplaysByTenant(final String tenantCode) {
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
        return this.displayRepository.findByBranchTenantCode(tenantCode);
    }

    @Override
<<<<<<< HEAD
    public Set<Display> getUnassignedDisplaysByTenant(String tenantCode) {
=======
    public Set<Display> getUnassignedDisplaysByTenant(final String tenantCode) {
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
        return this.displayRepository.findByBranchTenantCodeAndTellerStationIsNull(tenantCode);
    }

    @Override
<<<<<<< HEAD
=======
    public Set<Display> getUnassignedDisplaysByBranch(final long branchId) {
        return this.displayRepository.findAllByBranch_IdAndTellerStationIsNull(branchId);
    }

    @Override
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
    public Display updateDisplay(final long displayId, final String name) throws Exception {
        final Display existingDisplay = displayRepository.findById(displayId)
                .orElseThrow(() -> new Exception("Display with id: " + displayId + " doesn't exist."));

        existingDisplay.setName(name);

        return this.displayRepository.save(existingDisplay);
    }

    @Override
<<<<<<< HEAD
    public void removeDisplay(long displayId) {
=======
    public void removeDisplay(final long displayId) {
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
        final Display display = this.displayRepository.findById(displayId)
                .orElseThrow(EntityNotFoundException::new);

        if (display.getTellerStation() != null) {
            final TellerStation tellerStation = display.getTellerStation();
            tellerStation.setDisplay(null);
            this.tellerStationRepository.save(tellerStation);
        }

        this.displayRepository.deleteById(displayId);
    }
}
