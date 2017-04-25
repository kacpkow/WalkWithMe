package com.example.kacper.walkwithme.RegistrationActivity;

/**
 * Created by kacper on 2017-03-31.
 */

public interface RegistrationPresenter<T extends RegistrationView> {
    void validateCredentials(String username, String password, String confirmation, String email);
    void onDestroy();
}
