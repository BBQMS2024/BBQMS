package ba.unsa.etf.si.bbqms.ws.params.branch;

import java.time.Instant;
import java.util.Optional;

public record QueueStateParams(Optional<Long> serviceId, Optional<Instant> createdAfter, Optional<Instant> createdBefore) {
}
