package com.example.kacper.walkwithme.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kacper.walkwithme.MainActivity.MainView;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RegistrationActivity.RegistrationActivity;
import com.example.kacper.walkwithme.RequestController;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class LoginActivity extends AppCompatActivity implements LoginView {

    private LoginPresenter presenter;

    private Button submitButton;
    private Button registerButton;
    private EditText login;
    private EditText password;
    Integer usrId = 0;

    String loginState;

    OkHttpClient client;

    private static LoginActivity instance;
    public static LoginActivity get() { return instance; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_login);
        submitButton = (Button) findViewById(R.id.submitButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        login = (EditText) findViewById(R.id.loginField);
        password = (EditText) findViewById(R.id.passwordField);

        CookieHandler.setDefault(new CookieManager());

        presenter = new LoginPresenterImpl(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.validateCredentials(login.getText().toString(), password.getText().toString());
                login.setText("");
                password.setText("");
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences settings = getSharedPreferences
                ("USER_ID", Context.MODE_PRIVATE);
        usrId = settings.getInt("userId", 0);
        loginState = settings.getString("state", "out");

        settings = getSharedPreferences
                ("CREDENTIALS", Context.MODE_PRIVATE);
        String credentials = settings.getString("values", "");

        if(!credentials.equals("")){
            Intent intent = new Intent(getApplicationContext(), MainView.class);
            startActivity(intent);
            Log.e("Starting", "main view");
        }

        client = RequestController.getInstance().getClient();

    }

    public void getUsrData(){

        String url = getString(R.string.service_address) + "user";

        final Request request;
        request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                if(response.code() == 200){
                    backgroundThreadGoToOptions(getApplicationContext());
                }

            }
        });

    }

    public void backgroundThreadGoToOptions(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    goToOptions(usrId);
                    finish();
                }
            });
        }
    }


    public void showToast(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void goToOptions(Integer userId){
        login.setText("");
        password.setText("");
        Intent intent = new Intent(getApplicationContext(), MainView.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    @Override
    public Context getAppContext() {
        return getActivityContext();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public String getResourceStringValue(int id) {
        return getString(id);
    }

    @Override
    protected void onResume(){
        SharedPreferences settings = getSharedPreferences
                ("USER_ID", Context.MODE_PRIVATE);
        usrId = settings.getInt("userId", 0);
        if(usrId != 0){
            finish();
        }
        super.onResume();

    }

}





