package com.example.kacper.walkwithme.LoginActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.kacper.walkwithme.MainActivity.MainView;
import com.example.kacper.walkwithme.Model.User;
import com.example.kacper.walkwithme.RequestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.CookieHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by kacper on 2017-03-31.
 */

public class LoginPresenterImpl implements LoginPresenter{
    private LoginView loginView;
    ProgressDialog progressDialog;
    String json;

    OkHttpClient client;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        client = RequestController.getInstance().getClient();
    }

    @Override
    public void validateCredentials(String username, String password) {
        if(validateFields(username, password)){
            progressDialog = new ProgressDialog(loginView.getActivityContext());
            progressDialog.setTitle("Logging, please wait ...");
            progressDialog.show();
            String url ="http://10.0.2.2:8080/rest/login";

            LoginContent log = new LoginContent(username, password);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(log));

            final Request request;
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .build();

            client = RequestController.getInstance().getClient();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    progressDialog.dismiss();
                    backgroundThreadShortToast(loginView.getAppContext(), "Error in logging occured");
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    Gson retGson = new Gson();
                    progressDialog.dismiss();

                    if(response.code() == 200){
                        getUser();
                    }
                    else{
                        backgroundThreadShortToast(loginView.getAppContext(),"bad logging data 1");
                    }

                }
            });

        }
    }

    public void getUser(){

        String url ="http://10.0.2.2:8080/user";
        Gson gson = new Gson();

        MediaType mediaType = MediaType.parse("application/json");

        final Request request;
        request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                backgroundThreadShortToast(loginView.getAppContext(), "Error in logging occured");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Gson retGson = new Gson();
                progressDialog.dismiss();
                json = response.body().string();

                Log.e("response login", "was response in get data");
                Log.e("response get data", json);

                if(response.code() == 200){

                    User usr = retGson.fromJson(json, User.class);

                    SharedPreferences settings = loginView.getActivityContext().getSharedPreferences("userId", Context.MODE_PRIVATE);
                    if(settings != null){
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("ID", usr.getUser_id());
                        editor.putString("state", "logged in");
                        editor.commit();
                    }
                    RequestController.getInstance().setState(true);

                    backgroundThreadStartMainActivity(loginView.getAppContext(), usr.getUser_id());

                }
                else{
                    Log.e("get data", json);
                }

            }
        });

    }

    public static void backgroundThreadShortToast(final Context context,
                                                  final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void backgroundThreadStartMainActivity(final Context context, final int user_Id) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    SharedPreferences settings = context.getSharedPreferences("USER_ID", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("userId", user_Id);
                    editor.commit();

                    Intent intent = new Intent(context, MainView.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("USER_ID", user_Id);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    public Boolean validateFields(String username, String password){
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            if(TextUtils.isEmpty(username)){
                loginView.showToast("Empty login field\nTry again");
                return false;
            }
            else{
                loginView.showToast("Empty password field\nTry again");
                return false;
            }
        }
        return true;
    }

}