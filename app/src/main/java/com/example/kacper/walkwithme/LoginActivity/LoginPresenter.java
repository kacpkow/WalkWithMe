package com.example.kacper.walkwithme.LoginActivity;

/**
 * Created by kacper on 2017-03-31.
 */

public interface LoginPresenter<T extends LoginView> {
    void validateCredentials(String username, String password);
    void onDestroy();
}
