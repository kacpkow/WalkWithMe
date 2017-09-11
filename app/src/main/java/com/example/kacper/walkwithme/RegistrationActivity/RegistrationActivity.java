package com.example.kacper.walkwithme.RegistrationActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kacper.walkwithme.LoginActivity.LoginActivity;
import com.example.kacper.walkwithme.Model.RegistrationForm;
import com.example.kacper.walkwithme.R;

import java.lang.String;

public class RegistrationActivity extends AppCompatActivity implements RegistrationView {

    private RegistrationPresenter presenter;

    private EditText nick;
    private EditText firstPassword;
    private EditText secondPassword;
    private EditText firstName;
    private EditText lastName;
    private EditText city;
    private EditText email;
    private EditText dateOfBirth;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        nick = (EditText) findViewById(R.id.loginTextField);
        firstName = (EditText) findViewById(R.id.firstNameTextField);
        lastName = (EditText) findViewById(R.id.lastNameTextField);
        city = (EditText) findViewById(R.id.cityTextField);
        dateOfBirth = (EditText) findViewById(R.id.dateOfBirthTextField);
        firstPassword = (EditText) findViewById(R.id.firstPasswordTextField);
        secondPassword = (EditText) findViewById(R.id.secondPasswordTextField);
        email = (EditText) findViewById(R.id.emailTextField);
        registerButton = (Button) findViewById(R.id.registerButton);

        presenter = new RegistrationPresenterImpl(this);
        registerButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                RegistrationForm form = new RegistrationForm();
                form.setFirstName(firstName.getText().toString());
                form.setLastName(lastName.getText().toString());
                form.setNick(nick.getText().toString());
                form.setCity(city.getText().toString());
                form.setMail(email.getText().toString());
                form.setDate(dateOfBirth.getText().toString());
                form.setPassword(firstPassword.getText().toString());

                if(secondPassword.getText().toString().equals(firstPassword.getText().toString())){
                    presenter.register(form);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Second password is not the same as first", Toast.LENGTH_SHORT).show();
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

    @Override
    public void finishActivity() {
        finish();
    }

}
