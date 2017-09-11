package com.example.kacper.walkwithme.MainActivity.Announcements;

/**
 * Created by kacper on 2017-07-04.
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
