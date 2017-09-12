package com.example.kacper.walkwithme.MainActivity.Announcements;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class AnnouncementsPresenterImpl implements  AnnouncementsPresenter {
    private AnnouncementsView announcementsView;

    public AnnouncementsPresenterImpl(AnnouncementsView announcementsView){
        this.announcementsView = announcementsView;
    }

    @Override
    public void onDestroy() {
        this.announcementsView = null;
    }

    @Override
    public void refreshElements() {
        announcementsView.refreshElements();
    }
}
