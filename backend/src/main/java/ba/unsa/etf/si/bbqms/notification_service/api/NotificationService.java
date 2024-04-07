package ba.unsa.etf.si.bbqms.notification_service.api;

public interface NotificationService<T> {
    boolean sendNotification(final String deviceIdentifier, final String title, final T body);
}
