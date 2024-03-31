package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.DisplayService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Display;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.repository.DisplayRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
public class DefaultDisplayService implements DisplayService {
    private final DisplayRepository displayRepository;
    private final TellerStationRepository tellerStationRepository;
    private final TenantRepository tenantRepository;

    public DefaultDisplayService(final DisplayRepository displayRepository,
                                 final TellerStationRepository tellerStationRepository,
                                 final TenantRepository tenantRepository) {
        this.displayRepository = displayRepository;
        this.tellerStationRepository = tellerStationRepository;
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Display createDisplay(final String name, final Long tellerStationId) throws Exception {
        TellerStation tellerStation = tellerStationRepository.findById(tellerStationId)
                .orElseThrow(() -> new Exception("Teller station with id: " + tellerStationId + " doesn't exist."));

        Branch branch = tellerStation.getBranch();

        final Display newDisplay = new Display(name, tellerStation, branch);

        return this.displayRepository.save(newDisplay);
    }
}
