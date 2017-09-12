package com.example.kacper.walkwithme.MainActivity.Notifications;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class NotificationsPresenterImpl implements NotificationsPresenter {
    private NotificationsView notificationsView;
    public NotificationsPresenterImpl(NotificationsView notificationsView){ this.notificationsView = notificationsView;}

    @Override
    public void onDestroy() {
        this.notificationsView = null;
    }

    @Override
    public void refreshAdapterElements() {
        notificationsView.refreshElements();
    }

}
