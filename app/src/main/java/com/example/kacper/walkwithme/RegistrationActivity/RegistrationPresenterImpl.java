package com.example.kacper.walkwithme.RegistrationActivity;

import android.text.TextUtils;

/**
 * Created by kacper on 2017-03-31.
 */

public class RegistrationPresenterImpl implements RegistrationPresenter, RegistrationInteractor.OnRegistrationFinishedListener{

    private RegistrationView registrationView;
    private RegistrationInteractor registrationInteractor;

    public RegistrationPresenterImpl(RegistrationView registrationView) {
        this.registrationView = registrationView;
        this.registrationInteractor = new RegistrationInteractorImpl();
    }

    @Override
    public void validateCredentials(String username, String password, String confirmation, String email) {
        if(registrationView != null){
            //mainView.showToast("Logging");
        }
        registrationInteractor.login(username, password, confirmation, email, this);
    }

    @Override
    public void onDestroy() {
        registrationView = null;
    }

    @Override
    public void onUsernameError() {
        if(registrationView != null){
            registrationView.showToast("Username error");
        }
    }

    @Override
    public void onPasswordError() {
        if(registrationView != null){
            registrationView.showToast("Password error");
        }
    }

    @Override
    public void onPasswordConfirmationError() {
        if(registrationView != null){
            registrationView.showToast("Password confirmation error");
        }
    }

    @Override
    public void onEmailError() {
        if(registrationView != null){
            registrationView.showToast("Invalid email");
        }
    }

    @Override
    public void onSuccess() {
        registrationView.goToLoginScreen();
    }
}
