package com.example.kacper.walkwithme.LoginActivity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kacper.walkwithme.MainActivity.MainView;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RegistrationActivity.RegistrationActivity;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private LoginPresenter presenter;

    private Button submitButton;
    private Button registerButton;
    private EditText login;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        submitButton = (Button) findViewById(R.id.submitButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        login = (EditText) findViewById(R.id.loginField);
        password = (EditText) findViewById(R.id.passwordField);

        presenter = new LoginPresenterImpl(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.validateCredentials(login.getText().toString(), password.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showToast(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void goToOptions(){
        login.setText("");
        password.setText("");
        Intent intent = new Intent(getApplicationContext(), MainView.class);
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

}





