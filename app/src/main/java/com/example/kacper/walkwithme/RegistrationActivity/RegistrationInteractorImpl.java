package com.example.kacper.walkwithme.RegistrationActivity;

import android.os.Handler;
import android.text.TextUtils;

/**
 * Created by kacper on 2017-03-31.
 */

public class RegistrationInteractorImpl implements RegistrationInteractor{
    @Override
    public void login(final String username, final String password, final String passwordConfirmation, final String email, final OnRegistrationFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                boolean error = false;
                if (TextUtils.isEmpty(username)){
                    listener.onUsernameError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 15){
                    listener.onPasswordError();
                    error = true;
                    return;
                }
                if(TextUtils.isEmpty(passwordConfirmation) || !TextUtils.equals(password, passwordConfirmation)){
                    listener.onPasswordConfirmationError();
                    error = true;
                    return;
                }
                if(!isValidEmail(email)){
                    listener.onEmailError();
                    error = true;
                    return;
                }
                if (!error){
                    listener.onSuccess();
                }
            }
        }, 2000);
    }
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
