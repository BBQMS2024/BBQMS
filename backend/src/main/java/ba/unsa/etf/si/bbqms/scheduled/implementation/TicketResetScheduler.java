package ba.unsa.etf.si.bbqms.scheduled.implementation;

import ba.unsa.etf.si.bbqms.ticket_service.api.TicketService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TicketResetScheduler {
    private static final Logger logger = LoggerFactory.getLogger(TicketResetScheduler.class);
    private final TicketService ticketService;
    public TicketResetScheduler(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void run() {
        logger.info("Running ticket reset task scheduler.");
        try {
            this.ticketService.deleteAllTickets();
            logger.info("Tickets reset.");
        } catch (final Exception exception) {
            logger.error("Failed to reset tickets.");
        }
    }
}
