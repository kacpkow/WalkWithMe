package com.example.kacper.walkwithme.MainActivity.Announcements;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public interface AnnouncementsPresenter<T extends AnnouncementsView> {
    public void onDestroy();
    void refreshElements();
}
