package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.DisplayService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Display;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.repository.BranchRepository;
import ba.unsa.etf.si.bbqms.repository.DisplayRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

        final Display newDisplay = new Display(name, null, branch);

        return this.displayRepository.save(newDisplay);
    }

    @Override
    public Set<Display> getDisplays(final long branchId) throws Exception {
        this.branchRepository.findById(branchId)
                .orElseThrow(() -> new Exception("Branch with id: " + branchId + " doesn't exist."));

        return this.displayRepository.findByBranchId(branchId);
    }

    @Override
    public Set<Display> getDisplaysByTenant(String tenantCode) {
        return this.displayRepository.findByBranchTenantCode(tenantCode);
    }

    @Override
    public Display updateDisplay(final long displayId, final String name) throws Exception {
        final Display existingDisplay = displayRepository.findById(displayId)
                .orElseThrow(() -> new Exception("Display with id: " + displayId + " doesn't exist."));

        existingDisplay.setName(name);

        return this.displayRepository.save(existingDisplay);
    }

    @Override
    public void removeDisplay(long displayId) {
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
