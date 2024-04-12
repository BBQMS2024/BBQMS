package ba.unsa.etf.si.bbqms.notification_service.implementation;

import ba.unsa.etf.si.bbqms.notification_service.api.Notification;
import ba.unsa.etf.si.bbqms.notification_service.api.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockNotificationService implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(MockNotificationService.class);

    @Override
    public boolean sendNotification(final Notification notification) {
        logger.info("Pretending to send a notification to recipient: " + notification.recipient());
        return true;
    }
}
