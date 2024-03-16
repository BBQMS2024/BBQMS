package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.queue_service.api.QueueService;
import ba.unsa.etf.si.bbqms.ws.models.DummyDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/teller")
public class TellerController {
    private final QueueService queueService;

    public TellerController(final QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping
    public DummyDto example() {
        String example = queueService.dummyQueueServiceImpl();
        return new DummyDto(example);
    }
}
