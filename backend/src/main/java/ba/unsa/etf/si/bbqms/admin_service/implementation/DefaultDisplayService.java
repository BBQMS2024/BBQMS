package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.DisplayService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Display;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.repository.DisplayRepository;
import ba.unsa.etf.si.bbqms.repository.TellerStationRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultDisplayService implements DisplayService {
    private final DisplayRepository displayRepository;
    private final TellerStationRepository tellerStationRepository;

    public DefaultDisplayService(final DisplayRepository displayRepository,
                                 final TellerStationRepository tellerStationRepository,
                                 final TenantRepository tenantRepository) {
        this.displayRepository = displayRepository;
        this.tellerStationRepository = tellerStationRepository;
    }

    @Override
    public Display createDisplay(final String name, final long tellerStationId) throws Exception {
        final TellerStation tellerStation = tellerStationRepository.findById(tellerStationId)
                .orElseThrow(() -> new Exception("Teller station with id: " + tellerStationId + " doesn't exist."));

        if(tellerStation.getDisplay() != null)
            throw new Exception("Teller station with id: " + tellerStationId + " already has a display assigned to it.");

        final Branch branch = tellerStation.getBranch();

        final Display newDisplay = new Display(name, tellerStation, branch);
        tellerStation.setDisplay(newDisplay);

        return this.displayRepository.save(newDisplay);
    }

    @Override
    public Display getDisplay(final long displayId) throws Exception {
        final Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new Exception("Display with id: " + displayId + " doesn't exist."));

        return display;
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
        final Optional<TellerStation> optionalTellerStation = this.tellerStationRepository.findByDisplayId(displayId);

        if(optionalTellerStation.isEmpty())
            return;

        final TellerStation tellerStation = optionalTellerStation.get();
        tellerStation.setDisplay(null);

        this.tellerStationRepository.save(tellerStation);

        this.displayRepository.deleteById(displayId);
    }
}
