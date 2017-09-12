package com.example.kacper.walkwithme.RegistrationActivity;

import android.content.Context;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public interface RegistrationView {
    void showToast(String msg);
    void goToLoginScreen();
    Context getActivityContext();
    Context getAppContext();
    void finishActivity();
}
