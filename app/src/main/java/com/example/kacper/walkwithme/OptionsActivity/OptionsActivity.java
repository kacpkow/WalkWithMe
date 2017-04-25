package com.example.kacper.walkwithme.OptionsActivity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.SettingsActivity.SettingsActivity;

public class OptionsActivity extends FragmentActivity implements OptionsView{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        ImageButton logoutButton = (ImageButton) findViewById(R.id.logoutButton);
        ImageButton newWalkButton = (ImageButton) findViewById(R.id.newWalkButton);
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        ImageButton chatButton = (ImageButton) findViewById(R.id.chatButton);
        final Animation animScaleSettings = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        final Animation animScaleNewWalk = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        final Animation animScaleMessages = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        final Animation animScaleLogout = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleLogout);
                animScaleLogout.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        newWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleNewWalk);
                animScaleNewWalk.setAnimationListener(new Animation.AnimationListener() {
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

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleSettings);
                animScaleSettings.setAnimationListener(new Animation.AnimationListener() {
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
                v.startAnimation(animScaleMessages);
            }
        });
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

}
