package com.example.kacper.walkwithme.LoginActivity;

import com.android.volley.toolbox.StringRequest;
import com.example.kacper.walkwithme.GsonRequest;
import com.example.kacper.walkwithme.User;

/**
 * Created by kacper on 2017-03-31.
 */

public interface LoginPresenter<T extends LoginView> {
    void validateCredentials(String username, String password);
    void onDestroy();
    GsonRequest<User> getGsonRequest();
    StringRequest getStrRequest();
}
