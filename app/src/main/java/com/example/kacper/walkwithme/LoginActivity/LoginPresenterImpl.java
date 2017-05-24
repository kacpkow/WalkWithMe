package com.example.kacper.walkwithme.LoginActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.kacper.walkwithme.MainActivity.MainView;
import com.example.kacper.walkwithme.MapsActivity;
import com.example.kacper.walkwithme.Model.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by kacper on 2017-03-31.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView loginView;
    ProgressDialog progressDialog;
    String json;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void validateCredentials(String username, String password) {
        if(validateFields(username, password)){
            progressDialog = new ProgressDialog(loginView.getActivityContext());
            progressDialog.setTitle("Logging, please wait ...");
            progressDialog.show();
            String url ="http://10.0.2.2:8080/LoginAndroid";
            OkHttpClient client = new OkHttpClient();

            Gson gson = new Gson();

            LoginContent log = new LoginContent(username, password);
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(log));
            //backgroundThreadStartMainActivity(loginView.getAppContext(), 0);

            final Request request;
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
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

                    String jsonString = "{\"error\":\"notAnUser\"}";
                    if(json.equals(jsonString) == false){

                        User usr = retGson.fromJson(json, User.class);
                        SharedPreferences settings = loginView.getActivityContext().getSharedPreferences("userId", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("ID", usr.getUser_id());
                        editor.commit();
                        backgroundThreadStartMainActivity(loginView.getAppContext(), usr.getUser_id());
                    }
                    else{
                        backgroundThreadShortToast(loginView.getAppContext(),"bad logging data");
                    }

                }
            });

        }
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
                    Intent intent = new Intent(context, MainView.class);
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
