package ba.unsa.etf.si.bbqms.queue_service.api;

import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Ticket;

import java.util.Optional;

public interface QueueService {
    Optional<Ticket> findNextTicketForStation(final TellerStation tellerStation);
    void removeActiveTicketFromQueue(final TellerStation tellerStation);
    Optional<Ticket> advanceQueueState(final TellerStation tellerStation);
    Optional<Ticket> findCurrentTicketForStation(TellerStation tellerStation);
}
