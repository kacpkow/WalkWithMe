package com.example.kacper.walkwithme.LoginActivity;

import android.content.Context;

/**
 * Created by kacper on 2017-03-31.
 */

public interface LoginView {
    void showToast(String msg);
    void goToOptions();
    Context getAppContext();
    Context getActivityContext();
    String returnLogin();
    String returnPassword();
}
