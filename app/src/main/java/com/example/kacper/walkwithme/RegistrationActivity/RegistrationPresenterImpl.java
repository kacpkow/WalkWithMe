package com.example.kacper.walkwithme.RegistrationActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

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

public class RegistrationPresenterImpl implements RegistrationPresenter{

    private RegistrationView registrationView;
    ProgressDialog progressDialog;

    public Integer getFlag() {
        return flag;
    }

    @Override
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    private Integer flag;

    public RegistrationPresenterImpl(RegistrationView registrationView) {
        this.registrationView = registrationView;
        setFlag(0);
        progressDialog = new ProgressDialog(registrationView.getActivityContext());
    }

    @Override
    public Boolean register(String username, String password, String confirmation, String email) {
        if(validateFields(username, password, confirmation, email)){
            progressDialog = new ProgressDialog(registrationView.getActivityContext());
            String url ="http://10.0.2.2:8080/RegisterAndroid";
            progressDialog.setTitle("Account registering, please wait ...");
            progressDialog.show();
            OkHttpClient client = new OkHttpClient();

            Gson gson = new Gson();

            RegistrationContent reg = new RegistrationContent(username,password,email);
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(reg));

            Request request;
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
                    backgroundThreadShortToast(registrationView.getAppContext(),"Error in registration occured");
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    progressDialog.dismiss();
                    String json = response.body().string();
                    backgroundThreadShortToast(registrationView.getAppContext(),json);
                    setFlag(1);
                }
            });

        }
        if(getFlag() == 1)
            return true;

        return false;
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

    public Boolean validateFields(String username, String password, String confirmation, String email){
        if(TextUtils.isEmpty(username)){
            registrationView.showToast("Empty nick field\nTry again");
            return false;
        }
        if (TextUtils.isEmpty(password) || (password.length() < 6) || password.length() > 15){
            registrationView.showToast("Your password does not match to the requirements\nTry again");
            return false;
        }
        if(TextUtils.isEmpty(confirmation) || !TextUtils.equals(password, confirmation)){
            registrationView.showToast("Your password confirmation is not the same as the password\nTry again");
            return false;
        }
        if(!isValidEmail(email)){
            registrationView.showToast("Your email is not correct\nTry again");
            return false;
        }
        return true;
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onDestroy() {
        registrationView = null;
    }

}
