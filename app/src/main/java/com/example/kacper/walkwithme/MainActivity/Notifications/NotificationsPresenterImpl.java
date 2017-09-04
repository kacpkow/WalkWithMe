package com.example.kacper.walkwithme.MainActivity.Notifications;

/**
 * Created by kacper on 2017-07-04.
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
