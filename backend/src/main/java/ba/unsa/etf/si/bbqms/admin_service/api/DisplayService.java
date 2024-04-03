package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.Display;

import java.util.Set;

public interface DisplayService {
    Display createDisplay(final String name, final long branchId) throws Exception;
    Set<Display> getDisplays(final long branchId) throws Exception;
    Set<Display> getAllDisplays();
    Display updateDisplay(final long displayId, final String name) throws Exception;
    void removeDisplay(final long displayId);
}
