package com.example.kacper.walkwithme.MainActivity.Announcements;

/**
 * Created by kacper on 2017-07-04.
 */

public interface AnnouncementsPresenter<T extends AnnouncementsView> {
    public void onDestroy();
    void refreshElements();
}
