package com.example.kacper.walkwithme.RegistrationActivity;

import com.example.kacper.walkwithme.Model.RegistrationForm;

/**
 * Created by kacper on 2017-03-31.
 */

public interface RegistrationPresenter<T extends RegistrationView> {
    void register(RegistrationForm form);
    void onDestroy();
}
