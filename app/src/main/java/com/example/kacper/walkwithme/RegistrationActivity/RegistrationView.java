package com.example.kacper.walkwithme.RegistrationActivity;

import android.content.Context;

/**
 * Created by kacper on 2017-03-31.
 */

public interface RegistrationView {
    void showToast(String msg);
    void goToLoginScreen();
    Context getActivityContext();
    Context getAppContext();
    void finishActivity();
}
