package com.example.kacper.walkwithme.Services;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public interface NotificationCheckerCallbacks {
    void updateNotificationFromNotifications();
    void updateMessageNotificationsFromService(String json);
}
