package com.example.kacper.walkwithme.RegistrationActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kacper.walkwithme.LoginActivity.LoginActivity;
import com.example.kacper.walkwithme.R;

import java.lang.String;

public class RegistrationActivity extends AppCompatActivity implements RegistrationView {

    private RegistrationPresenter presenter;

    private EditText login;
    private EditText firstPassword;
    private EditText secondPassword;
    private EditText firstName;
    private EditText lastName;
    private EditText city;
    private EditText email;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = (EditText) findViewById(R.id.loginTextField);
        firstName = (EditText) findViewById(R.id.firstNameTextField);
        lastName = (EditText) findViewById(R.id.lastNameTextField);
        city = (EditText) findViewById(R.id.cityTextField);
        firstPassword = (EditText) findViewById(R.id.firstPasswordTextField);
        secondPassword = (EditText) findViewById(R.id.secondPasswordTextField);
        email = (EditText) findViewById(R.id.emailTextField);
        registerButton = (Button) findViewById(R.id.registerButton);

        presenter = new RegistrationPresenterImpl(this);
        registerButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                boolean registeredCheck = presenter.register(
                        login.getText().toString(),
                        firstPassword.getText().toString(),
                        secondPassword.getText().toString(),
                        email.getText().toString());

                if(registeredCheck){
                    presenter.setFlag(0);
                    goToLoginScreen();
                }
            }
        });
    }

    @Override
    public void showToast(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void goToLoginScreen() {
        this.finish();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public Context getAppContext() {
        return getActivityContext();
    }

}
