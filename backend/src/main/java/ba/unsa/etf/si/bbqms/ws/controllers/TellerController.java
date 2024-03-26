package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.queue_service.api.QueueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/teller")
public class TellerController {
    private final QueueService queueService;

    public TellerController(final QueueService queueService) {
        this.queueService = queueService;
    }
}
