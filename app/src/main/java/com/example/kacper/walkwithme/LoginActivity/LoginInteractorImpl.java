package com.example.kacper.walkwithme.LoginActivity;

/**
 * Created by kacper on 2017-03-31.
 */

public class LoginInteractorImpl implements LoginInteractor {
    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener) {
        // Mock login. I'm creating a handler to delay the answer a couple of seconds
                boolean error = false;

                /*
                if (TextUtils.isEmpty(username)){
                    listener.onUsernameError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    listener.onPasswordError();
                    error = true;
                    return;
                }
                if (!error){
                    listener.onSuccess();
                }
                */
    }


}