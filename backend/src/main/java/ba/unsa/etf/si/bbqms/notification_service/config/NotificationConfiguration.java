package ba.unsa.etf.si.bbqms.notification_service.config;

import ba.unsa.etf.si.bbqms.notification_service.api.NotificationService;
import ba.unsa.etf.si.bbqms.notification_service.implementation.ExpoNotificationService;
import ba.unsa.etf.si.bbqms.notification_service.implementation.MockNotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NotificationConfiguration {
    @Value("${notifications.expo-url}")
    public String expoPushUrl;

    @Bean
    @ConditionalOnProperty(prefix = "notifications", name = "mock", havingValue = "false")
    public NotificationService notificationService(final RestTemplate restTemplate) {
        return new ExpoNotificationService(this.expoPushUrl, restTemplate);
    }

    @Bean
    @ConditionalOnProperty(prefix = "notifications", name = "mock", havingValue = "true", matchIfMissing = true)
    public NotificationService mockNotificationService() {
        return new MockNotificationService();
    }
}
