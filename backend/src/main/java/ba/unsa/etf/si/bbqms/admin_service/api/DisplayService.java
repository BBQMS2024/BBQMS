package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.Display;

public interface DisplayService {
    Display createDisplay(final String name, final Long tellerStationId) throws Exception;
}
