package com.example.kacper.walkwithme.PersonDetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kacper.walkwithme.MakeStrollActivity.MakeStrollActivity;
import com.example.kacper.walkwithme.R;

public class PersonDetailsActivity extends AppCompatActivity {

    private TextView nick;
    private TextView name;
    private TextView age;
    private TextView description;
    private TextView location;
    private ImageView image;
    private Button strollButton;
    private Integer userId;
    private String userAge;
    private String userLocation;
    private String userDescription;
    private String userNick;
    private String userName;
    private String userImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        nick = (TextView)findViewById(R.id.userNick);
        name = (TextView)findViewById(R.id.userName);
        age = (TextView)findViewById(R.id.userAge);
        description = (TextView)findViewById(R.id.userDescription);
        location = (TextView)findViewById(R.id.userLocation);
        image = (ImageView)findViewById(R.id.photo);
        strollButton = (Button)findViewById(R.id.strollButton);

        Bundle b = getIntent().getExtras();

        userId = b.getInt("USER_ID");
        userAge = b.getString("USER_AGE");
        userLocation = b.getString("USER_LOCATION");
        userDescription= b.getString("USER_DESCRIPTION");
        userName = b.getString("USER_NAME");
        userImageUrl = b.getString("USER_IMAGE");

        nick.setText(userId.toString());
        name.setText(userName);
        age.setText(userAge);
        location.setText(userLocation);
        description.setText(userDescription);

        Glide.with(this).load(userImageUrl)
                .into(image);

        strollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeStrollActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("USER_NAME", userName);
                startActivity(intent);
            }
        });
    }
}
