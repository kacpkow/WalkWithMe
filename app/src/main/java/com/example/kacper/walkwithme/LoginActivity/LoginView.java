package com.example.kacper.walkwithme.LoginActivity;

import android.content.Context;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public interface LoginView {
    void showToast(String msg);
    void goToOptions(Integer userId);
    Context getAppContext();
    Context getActivityContext();
    String getResourceStringValue(int id);
}
