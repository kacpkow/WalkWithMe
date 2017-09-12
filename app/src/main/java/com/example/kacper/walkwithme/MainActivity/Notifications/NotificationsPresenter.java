package com.example.kacper.walkwithme.MainActivity.Notifications;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public interface NotificationsPresenter<T extends NotificationsView> {
    void onDestroy();
    void refreshAdapterElements();
}
