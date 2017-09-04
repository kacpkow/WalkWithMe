package com.example.kacper.walkwithme.MainActivity.Notifications;

/**
 * Created by kacper on 2017-07-04.
 */

public interface NotificationsPresenter<T extends NotificationsView> {
    void onDestroy();
    void refreshAdapterElements();
}
