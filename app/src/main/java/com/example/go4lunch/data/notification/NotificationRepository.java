package com.example.go4lunch.data.notification;

public interface NotificationRepository {

    boolean isNotificationEnabled();

    void setNotificationEnabled(boolean enabled);

    void scheduleNotification(long delay);

    void cancelNotification();
}
