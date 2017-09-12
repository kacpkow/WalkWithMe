package com.example.kacper.walkwithme.RegistrationActivity;

import com.example.kacper.walkwithme.Model.RegistrationForm;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public interface RegistrationPresenter<T extends RegistrationView> {
    void register(RegistrationForm form);
    void onDestroy();
}
