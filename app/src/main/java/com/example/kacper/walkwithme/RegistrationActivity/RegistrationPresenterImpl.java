package com.example.kacper.walkwithme.RegistrationActivity;

import android.app.ProgressDialog;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kacper.walkwithme.GsonRequest;
import com.example.kacper.walkwithme.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kacper on 2017-03-31.
 */

public class RegistrationPresenterImpl implements RegistrationPresenter, RegistrationInteractor.OnRegistrationFinishedListener{

    private RegistrationView registrationView;
    private RegistrationInteractor registrationInteractor;
    ProgressDialog progressDialog;
    GsonRequest<User> myReq;

    public RegistrationPresenterImpl(RegistrationView registrationView) {
        this.registrationView = registrationView;
        this.registrationInteractor = new RegistrationInteractorImpl();
        progressDialog = new ProgressDialog(registrationView.getActivityContext());
    }

    @Override
    public void register(String username, String password, String confirmation, String email) {
        if(registrationView != null){
            String url ="http://10.0.2.2:8080/login.jsp";
            progressDialog.setTitle("Logging, please wait ...");
            progressDialog.show();
            Map<String, String> map = new HashMap<String, String>();
            map.put("req_type", "aLog");
            //map.put("firstName", loginView.returnLogin());
            //map.put("password", loginView.returnPassword());

            //myReq = new GsonRequest<User>(url,User.class, map, createSuccessListener(), createErrorListener());
        }
        registrationInteractor.login(username, password, confirmation, email, this);
    }

    private Response.Listener<User> createSuccessListener() {
        return new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                progressDialog.dismiss();
                //loginView.showToast("Logged to: " + response.getName());
                //loginView.showToast("Logged to: " + response.toString());
                //loginView.goToOptions();
            }
        };
    }

    @Override
    public void onDestroy() {
        registrationView = null;
    }

    @Override
    public StringRequest getStrRequest() {
        return null;
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
