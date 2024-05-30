package ba.unsa.etf.si.bbqms.queue_service.api;

import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Ticket;

import java.util.Optional;

public interface QueueService {
    Optional<Ticket> findNextTicketForStation(final TellerStation tellerStation);

    Optional<Ticket> advanceQueueState(final TellerStation tellerStation);

    Optional<Ticket> findCurrentTicketForStation(final TellerStation tellerStation);

    /**
     * Current ticket on the station is undo-ed (effectively teller station foreign key returned to null).
     * The last ticket which was assigned to the station is then returned (if there is one).
     * @param stationId - station whose state we want to return to previous
     * @return - the ticket which is now assigned to the station (if any)
     */
    Optional<Ticket> undoStationState(final long stationId);
}
