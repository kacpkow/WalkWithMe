package com.example.kacper.walkwithme.MainActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.example.kacper.walkwithme.MainActivity.Announcements.AnnouncementFragment;
import com.example.kacper.walkwithme.MainActivity.Chat.ChatFragment;
import com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments.ForcomingAppointmentsFragment;
import com.example.kacper.walkwithme.MainActivity.Notifications.NotificationsFragment;
import com.example.kacper.walkwithme.MainActivity.PersonsList.PersonsListFragment;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.Services.NotificationChecker;
import com.example.kacper.walkwithme.Services.NotificationCheckerCallbacks;
import com.example.kacper.walkwithme.SettingsActivity.SettingsActivity;

import sk.rogansky.logger.Log;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */
public class MainView extends AppCompatActivity implements NotificationCheckerCallbacks{

    private ImageButton chatButton;
    private ImageButton settingsButton;
    private ImageButton peopleButton;
    private ImageButton homeButton;
    private ImageButton notificationsButton;
    private ImageButton getAnnouncementsButton;

    private boolean launchMessagesWithNotification = false;

    String jsonMap;

    private int userId;
    private int currentFragmentOrdinal;

    private NotificationChecker notificationChecker;
    private boolean boundNotifications = false;

    long startTimeNotifications = 0;
    Handler timerHandlerNotifications = new Handler();
    Runnable timerRunnableNotifications = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTimeNotifications;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            if(boundNotifications){
                notificationChecker.checkNotifications();
                notificationChecker.checkMessages();
            }

            timerHandlerNotifications.postDelayed(this, 30000);
        }
    };

    @Override
    public void onBackPressed(){

        super.onBackPressed();
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        chatButton = (ImageButton) findViewById(R.id.chatButton);
        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        notificationsButton = (ImageButton) findViewById(R.id.notificationsButton);
        peopleButton = (ImageButton) findViewById(R.id.personButton);
        homeButton = (ImageButton) findViewById(R.id.homeButton);
        getAnnouncementsButton = (ImageButton)findViewById(R.id.getAnnouncementsButton);
        final Animation animScaleButton = AnimationUtils.loadAnimation(this, R.anim.anim_press_menu_button);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("USER_ID", Context.MODE_PRIVATE);
        userId = settings.getInt("userId", 0);
        final Bundle bundle = new Bundle();
        bundle.putInt("USER_ID", userId);

        Intent intent = new Intent(this, NotificationChecker.class);
        bindService(intent, serviceConnectionNotifications, Context.BIND_AUTO_CREATE);

        if (savedInstanceState != null) {
            return;
        }

        ForcomingAppointmentsFragment firstFragment = new ForcomingAppointmentsFragment();
        firstFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();

        homeButton.setColorFilter(0xffffbf00, PorterDuff.Mode.MULTIPLY);

        peopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleButton);
                animScaleButton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        peopleButton.setColorFilter(0xffffbf00, PorterDuff.Mode.MULTIPLY);
                        homeButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        chatButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        notificationsButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        getAnnouncementsButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);

                        PersonsListFragment newFragment = new PersonsListFragment();
                        newFragment.setArguments(bundle);
                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        if(currentFragmentOrdinal < 1){
                            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                        }
                        else{
                            transaction.setCustomAnimations(R.anim.enter_inverted, R.anim.exit_inverted, R.anim.pop_enter_inverted, R.anim.pop_exit_inverted);
                        }

                        android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (!(f instanceof PersonsListFragment)){
                            transaction.replace(R.id.fragment_container, newFragment, "detailsFragment");
                            transaction.commit();
                            currentFragmentOrdinal = 1;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleButton);
                animScaleButton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        currentFragmentOrdinal = 5;

                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleButton);
                animScaleButton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        homeButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        peopleButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        chatButton.setColorFilter(0xffffbf00, PorterDuff.Mode.MULTIPLY);
                        notificationsButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        getAnnouncementsButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);

                        ChatFragment newFragment = new ChatFragment();
                        if(launchMessagesWithNotification){
                            bundle.putString("messageNotifications", jsonMap);
                            launchMessagesWithNotification = false;
                        }
                        newFragment.setArguments(bundle);
                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        if(currentFragmentOrdinal < 2){
                            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                        }
                        else{
                            transaction.setCustomAnimations(R.anim.enter_inverted, R.anim.exit_inverted, R.anim.pop_enter_inverted, R.anim.pop_exit_inverted);
                        }

                        android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (!(f instanceof ChatFragment)){
                            transaction.replace(R.id.fragment_container, newFragment);
                            transaction.commit();
                            currentFragmentOrdinal = 2;
                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleButton);
                animScaleButton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        homeButton.setColorFilter(0xffffbf00, PorterDuff.Mode.MULTIPLY);
                        peopleButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        chatButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        notificationsButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        getAnnouncementsButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);

                        ForcomingAppointmentsFragment newFragment = new ForcomingAppointmentsFragment();
                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        transaction.setCustomAnimations(R.anim.enter_inverted, R.anim.exit_inverted, R.anim.pop_enter_inverted, R.anim.pop_exit_inverted);
                        android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (!(f instanceof ForcomingAppointmentsFragment)){
                            transaction.replace(R.id.fragment_container, newFragment);
                            transaction.commit();
                            currentFragmentOrdinal = 0;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleButton);
                animScaleButton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        homeButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        peopleButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        chatButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        notificationsButton.setColorFilter(0xffffbf00, PorterDuff.Mode.MULTIPLY);
                        getAnnouncementsButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);

                        NotificationsFragment newFragment = new NotificationsFragment();
                        newFragment.setArguments(bundle);
                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        if(currentFragmentOrdinal < 3){
                            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                        }
                        else{
                            transaction.setCustomAnimations(R.anim.enter_inverted, R.anim.exit_inverted, R.anim.pop_enter_inverted, R.anim.pop_exit_inverted);
                        }

                        android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (!(f instanceof NotificationsFragment)){
                            transaction.replace(R.id.fragment_container, newFragment);
                            transaction.commit();
                            currentFragmentOrdinal = 3;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        getAnnouncementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleButton);
                animScaleButton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        homeButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        peopleButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        chatButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        notificationsButton.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                        getAnnouncementsButton.setColorFilter(0xffffbf00, PorterDuff.Mode.MULTIPLY);

                        try{
                            Fragment previousFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            AnnouncementFragment newFragment = new AnnouncementFragment();
                            newFragment.setArguments(bundle);
                            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

                            android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                            if (!(f instanceof AnnouncementFragment)){
                                transaction.replace(R.id.fragment_container, newFragment);
                                transaction.commit();
                                currentFragmentOrdinal = 4;
                            }

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from service
        if (boundNotifications) {
            notificationChecker.setCallbacks(null); // unregister
            unbindService(serviceConnectionNotifications);
            boundNotifications = false;
        }
    }

    private ServiceConnection serviceConnectionNotifications = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            NotificationChecker.LocalBinder binder = (NotificationChecker.LocalBinder) service;
            notificationChecker = binder.getService();
            boundNotifications = true;
            notificationChecker.setCallbacks(MainView.this); // register
            timerHandlerNotifications.postDelayed(timerRunnableNotifications, 0);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            boundNotifications = false;
        }
    };

    @Override
    public void onDestroy(){
        timerHandlerNotifications.removeCallbacks(timerRunnableNotifications);
        super.onDestroy();
    }
    @Override
    public void onPause(){
        timerHandlerNotifications.removeCallbacks(timerRunnableNotifications);
        super.onPause();
    }
    @Override
    public void onResume(){
        timerHandlerNotifications.postDelayed(timerRunnableNotifications, 0);
        super.onResume();
    }


    @Override
    public void updateNotificationFromNotifications() {
        android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(f instanceof NotificationsFragment)){
            notificationsButton.setColorFilter(0xff0000ff, PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public void updateMessageNotificationsFromService(String json) {
        android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(f instanceof ChatFragment)){
            chatButton.setColorFilter(0xff0000ff, PorterDuff.Mode.MULTIPLY);
        }

        launchMessagesWithNotification = true;
        jsonMap = json;
    }
}
