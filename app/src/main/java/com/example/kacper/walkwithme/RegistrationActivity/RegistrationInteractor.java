package com.example.kacper.walkwithme.RegistrationActivity;


/**
 * Created by kacper on 2017-03-31.
 */

public interface RegistrationInteractor {
    interface OnRegistrationFinishedListener {
        void onUsernameError();

        void onPasswordError();

        void onPasswordConfirmationError();

        void onEmailError();

        void onSuccess();
    }

    boolean isValidEmail(CharSequence target);
    void login(String username, String password, String passwordConfirmation, String email, RegistrationInteractor.OnRegistrationFinishedListener listener);
}
