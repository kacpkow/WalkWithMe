package com.example.kacper.walkwithme.LoginActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kacper.walkwithme.GsonRequest;
import com.example.kacper.walkwithme.MainActivity.MainView;
import com.example.kacper.walkwithme.OptionsActivity.OptionsActivity;
import com.example.kacper.walkwithme.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

/**
 * Created by kacper on 2017-03-31.
 */

public class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnLoginFinishedListener {
    private LoginView loginView;
    private LoginInteractor mainInteractor;
    GsonRequest<User> myReq;
    ProgressDialog progressDialog;
    String json;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.mainInteractor = new LoginInteractorImpl();
        progressDialog = new ProgressDialog(loginView.getActivityContext());
    }

    @Override
    public void validateCredentials(String username, String password) {

        loginView.showToast("logowanie");
        progressDialog.setTitle("Logging, please wait ...");
        progressDialog.show();
        String url ="http://10.0.2.2:8080/login.jsp";
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("req_type","aLog")
                .add("nick", loginView.returnLogin())
                .add("password", loginView.returnPassword())
                .build();

        Request request;
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                loginView.showToast("error in logging occured");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                progressDialog.dismiss();
                json = response.body().string();
                backgroundThreadShortToast(loginView.getAppContext(),json);
            }
        });



    }

    public static void backgroundThreadShortToast(final Context context,
                                                  final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(context, MainView.class);
                    context.startActivity(intent);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
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

}
