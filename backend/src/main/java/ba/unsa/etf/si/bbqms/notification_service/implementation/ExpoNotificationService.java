package ba.unsa.etf.si.bbqms.notification_service.implementation;

import ba.unsa.etf.si.bbqms.notification_service.api.NotificationService;

public class ExpoNotificationService<T> implements NotificationService<T> {
    @Override
    public boolean sendNotification(final String deviceIdentifier, final String title, final T body) {
        return false;
    }
}
