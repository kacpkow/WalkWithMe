package com.example.kacper.walkwithme.LoginActivity;

import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kacper.walkwithme.GsonRequest;
import com.example.kacper.walkwithme.User;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by kacper on 2017-03-31.
 */

public class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnLoginFinishedListener {
    private LoginView loginView;
    private LoginInteractor mainInteractor;
    //JsonObjectRequest jsonObjReq;
    GsonRequest<User> myReq;
    ProgressDialog progressDialog;
    StringRequest strReq;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.mainInteractor = new LoginInteractorImpl();
        progressDialog = new ProgressDialog(loginView.getActivityContext());
    }

    @Override
    public void validateCredentials(String username, String password) {

//        String url = "http://json.org/JSONRequest.html";
//        progressDialog.setTitle("Logging, please wait ...");
//        progressDialog.show();
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("req_type_tag", "LIN");
//        map.put("login", loginView.returnLogin());
//        map.put("password", loginView.returnPassword());
//
//        myReq = new GsonRequest<User>(url,User.class, map, createSuccessListener(), createErrorListener());
        String url ="http://10.0.2.2:8080/home.jsp";
        strReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loginView.showToast(response.toString());
                        //progressDialog.setTitle(response.substring(0,500));
                        progressDialog.setTitle(response.toString());
                        progressDialog.show();
//        progressDialog.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error != null)
                    loginView.showToast("Error "+ error.toString() );
        }
        });


    }

    private Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error : " + error.getLocalizedMessage());
                progressDialog.dismiss();
                loginView.showToast(error.getLocalizedMessage());

            }
        };
    }

    private Response.Listener<User> createSuccessListener() {
        return new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                Log.i(TAG, "Response : " + response.getName());
                progressDialog.dismiss();
                //loginView.showToast("Logged to: " + response.getName());
                loginView.showToast("Logged to: " + response.toString());
                loginView.goToOptions();
            }
        };
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onUsernameError() {
        if(loginView != null){
            loginView.showToast("Username error");
        }
    }

    @Override
    public void onPasswordError() {
        if(loginView != null){
            loginView.showToast("Password error");
        }
    }

    @Override
    public void onSuccess() {
        loginView.goToOptions();
    }

    public GsonRequest<User> getGsonRequest(){
        return myReq;
    }

    public StringRequest getStrRequest(){
        return strReq;
    }

}
