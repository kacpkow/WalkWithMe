package com.example.kacper.walkwithme.PersonDetails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kacper.walkwithme.R;

public class PersonDetailsActivity extends AppCompatActivity {

    private TextView intentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        intentName = (TextView)findViewById(R.id.intentNr);
        Bundle b = getIntent().getExtras();
        Integer id = b.getInt("id");
        intentName.setText(id.toString());
    }
}
