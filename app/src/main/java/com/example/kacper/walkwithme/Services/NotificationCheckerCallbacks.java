package com.example.kacper.walkwithme.Services;

/**
 * Created by kacper on 2017-09-05.
 */

public interface NotificationCheckerCallbacks {
    void updateNotificationFromNotifications();
    void updateMessageNotificationsFromService(String json);
}
