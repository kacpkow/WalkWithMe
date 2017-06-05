package com.example.kacper.walkwithme.LoginActivity;

import android.content.Context;

/**
 * Created by kacper on 2017-03-31.
 */

public interface LoginView {
    void showToast(String msg);
    void goToOptions(Integer userId);
    Context getAppContext();
    Context getActivityContext();
}
