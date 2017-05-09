package com.example.kacper.walkwithme.RegistrationActivity;

/**
 * Created by kacper on 2017-03-31.
 */

public interface RegistrationPresenter<T extends RegistrationView> {
    Boolean register(String username, String password, String confirmation, String email);
    void onDestroy();
    void setFlag(Integer i);
}
