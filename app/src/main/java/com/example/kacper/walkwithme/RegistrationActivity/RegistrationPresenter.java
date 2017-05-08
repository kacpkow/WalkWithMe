package com.example.kacper.walkwithme.RegistrationActivity;

import com.android.volley.toolbox.StringRequest;
import com.example.kacper.walkwithme.GsonRequest;
import com.example.kacper.walkwithme.User;

/**
 * Created by kacper on 2017-03-31.
 */

public interface RegistrationPresenter<T extends RegistrationView> {
    void register(String username, String password, String confirmation, String email);
    void onDestroy();
    StringRequest getStrRequest();
    //GsonRequest<User> getGsonRequest();
}
