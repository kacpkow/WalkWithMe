package com.example.kacper.walkwithme.LoginActivity;

/**
 * Created by kacper on 2017-03-31.
 */

public interface LoginInteractor {
    interface OnLoginFinishedListener {
        void onUsernameError();

        void onPasswordError();

        void onSuccess();
    }

    void login(String username, String password, OnLoginFinishedListener listener);
}
