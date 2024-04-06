package ba.unsa.etf.si.bbqms.repository;

import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.domain.projections.TicketNumberProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<TicketNumberProjection> findTopByServiceInOrderByNumberDesc(final Set<Service> service);
}
