package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.Display;

public interface DisplayService {
    Display createDisplay(final String name, final long tellerStationId) throws Exception;
    Display getDisplay(final long displayId) throws Exception;
    Display updateDisplay(final long displayId, final String name) throws Exception;
    void removeDisplay(final long displayId);
}
