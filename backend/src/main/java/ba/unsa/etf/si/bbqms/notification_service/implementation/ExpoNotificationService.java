package ba.unsa.etf.si.bbqms.notification_service.implementation;

import ba.unsa.etf.si.bbqms.notification_service.api.Notification;
import ba.unsa.etf.si.bbqms.notification_service.api.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class ExpoNotificationService implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(ExpoNotificationService.class);
    private final String expoPushUrl;
    private final RestTemplate restTemplate;

    public ExpoNotificationService(final String expoPushUrl, final RestTemplate restTemplate) {
        this.expoPushUrl = expoPushUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean sendNotification(final Notification notification) {
        try {
            final ExpoResponse response = this.restTemplate.postForObject(
                    expoPushUrl,
                    new ExpoNotification(notification.recipient(), notification.title(), notification.body()),
                    ExpoResponse.class);

            if (response.data().status().equals("ok")) {
                logger.info("Successfully sent notification to recepient: " + notification.recipient());
                return true;
            } else {
                logger.warn("Could not send notification to recepient: " + notification.recipient());
                return false;
            }
        } catch (final Exception exception) {
            logger.warn("Could not send notification to recepient: " + notification.recipient());
            return false;
        }
    }

    public record ExpoNotification(String to, String title, String body) {
    }

    public record ExpoResponse(ExpoData data) {
        public record ExpoData(String status, String id) {
        }
    }
}
