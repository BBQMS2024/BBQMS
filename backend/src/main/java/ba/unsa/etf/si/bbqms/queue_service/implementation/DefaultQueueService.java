package ba.unsa.etf.si.bbqms.queue_service.implementation;

import ba.unsa.etf.si.bbqms.queue_service.api.QueueService;
import org.springframework.stereotype.Service;

@Service
public class DefaultQueueService implements QueueService {
    @Override
    public String dummyQueueServiceImpl() {
        return "Hello queue management app";
    }
}
