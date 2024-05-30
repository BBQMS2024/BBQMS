package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.ekapic1.stonebase.filter.CompositeField;
import ba.ekapic1.stonebase.filter.ConditionType;
import ba.unsa.etf.si.bbqms.admin_service.api.DisplayService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.BranchField;
import ba.unsa.etf.si.bbqms.domain.Display;
import ba.unsa.etf.si.bbqms.domain.DisplayField;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.TellerStationField;
import ba.unsa.etf.si.bbqms.domain.TenantField;
import ba.unsa.etf.si.bbqms.repository.BranchRepository;
import ba.unsa.etf.si.bbqms.repository.DisplayRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Display createDisplay(final String name, final long branchId) {
        final Branch branch = this.branchRepository.get(branchId);
        final Display newDisplay = new Display(name, branch);

        return this.displayRepository.save(newDisplay);
    }

    @Override
    public List<Display> getDisplays(final long branchId) {
        return this.displayRepository.findAll(
                this.displayRepository.filterBuilder()
                        .with(
                                CompositeField.of(DisplayField.BRANCH, BranchField.ID),
                                ConditionType.EQUAL,
                                branchId
                        )
                        .build()
        );
    }

    @Override
    public List<Display> getDisplaysByTenant(final String tenantCode) {
        return this.displayRepository.findAll(
                this.displayRepository.filterBuilder()
                        .with(
                                CompositeField.of(DisplayField.BRANCH, BranchField.TENANT, TenantField.CODE),
                                ConditionType.EQUAL,
                                tenantCode
                        )
                        .build()
        );
    }

    @Override
    public List<Display> getUnassignedDisplaysByTenant(final String tenantCode) {
        final List<Display> displays = this.displayRepository.findAll(
                this.displayRepository.filterBuilder()
                        .with(
                                CompositeField.of(DisplayField.BRANCH, BranchField.TENANT, TenantField.CODE),
                                ConditionType.EQUAL,
                                tenantCode
                        )
                        .build()
        );

        /*
            Display doesn't actually have a FK to tellerStation in the database, so we cannot use BaseRepository FilterBuilder
            to access the field. Because of this we must manually filter the displays here. Same below.
         */
        return displays.stream().filter(display -> display.getTellerStation() == null).toList();
    }

    @Override
    public List<Display> getUnassignedDisplaysByBranch(final long branchId) {
        final List<Display> displays = this.displayRepository.findAll(
                this.displayRepository.filterBuilder()
                        .with(
                                CompositeField.of(DisplayField.BRANCH, BranchField.ID),
                                ConditionType.EQUAL,
                                branchId
                        )
                        .build()
        );

        return displays.stream().filter(display -> display.getTellerStation() == null).toList();
    }

    @Override
    public Display updateDisplay(final long displayId, final String name) {
        final Display existingDisplay = displayRepository.get(displayId);
        existingDisplay.setName(name);

        return this.displayRepository.save(existingDisplay);
    }

    @Override
    public void removeDisplay(final long displayId) {
        final Display display = this.displayRepository.get(displayId);

        if (display.getTellerStation() != null) {
            final TellerStation tellerStation = display.getTellerStation();
            tellerStation.setDisplay(null);
            this.tellerStationRepository.save(tellerStation);
        }

        this.displayRepository.deleteById(displayId);
    }
}
