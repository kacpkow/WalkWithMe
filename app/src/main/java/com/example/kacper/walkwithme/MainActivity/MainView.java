package com.example.kacper.walkwithme.MainActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments.AppointmentsFragment;
import com.example.kacper.walkwithme.MainActivity.PersonsList.PersonsListFragment;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.SettingsActivity.SettingsActivity;

public class MainView extends AppCompatActivity {

    private ImageButton chatButton;
    private ImageButton settingsButton;
    private ImageButton peopleButton;
    private ImageButton homeButton;
    private TextView chatNotificationsView;
    private int userId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getIntExtra("USER_ID", 0);
        setContentView(R.layout.activity_main);

        chatButton = (ImageButton) findViewById(R.id.chatButton);
        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        chatNotificationsView = (TextView) findViewById(R.id.chatNotification);
        peopleButton = (ImageButton) findViewById(R.id.personButton);
        homeButton = (ImageButton) findViewById(R.id.homeButton);
        final Animation animScalePeopleButton = AnimationUtils.loadAnimation(this, R.anim.anim_press_menu_button);
        final Animation animScaleHomeButton = AnimationUtils.loadAnimation(this, R.anim.anim_press_menu_button);
        final Animation animScaleChatButton = AnimationUtils.loadAnimation(this, R.anim.anim_press_menu_button);
        final Animation animScaleSettingsButton = AnimationUtils.loadAnimation(this, R.anim.anim_press_menu_button);

        final Bundle bundle = new Bundle();
        bundle.putInt("USER_ID", userId);

        if (savedInstanceState != null) {
            return;
        }

        AppointmentsFragment firstFragment = new AppointmentsFragment();
        firstFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();

        peopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScalePeopleButton);
                animScalePeopleButton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        PersonsListFragment newFragment = new PersonsListFragment();
                        newFragment.setArguments(bundle);
                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (!(f instanceof PersonsListFragment)){
                            transaction.replace(R.id.fragment_container, newFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
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
                v.startAnimation(animScalePeopleButton);
                animScalePeopleButton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
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
                v.startAnimation(animScalePeopleButton);
                animScalePeopleButton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

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
                v.startAnimation(animScalePeopleButton);
                animScalePeopleButton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        AppointmentsFragment newFragment = new AppointmentsFragment();
                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (!(f instanceof AppointmentsFragment)){
                            transaction.replace(R.id.fragment_container, newFragment);
                            transaction.commit();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

    }

}
