package com.example.kacper.walkwithme.MakeStrollActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kacper.walkwithme.MainActivity.PersonsList.Person;
import com.example.kacper.walkwithme.MainActivity.PersonsList.SearchContent;
import com.example.kacper.walkwithme.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MakeStrollActivity extends AppCompatActivity {
    Button setDateButton;
    Button setTimeButton;
    Button setLocationButton;
    Button strollButton;
    private int year, month, day, hour, minute;
    private Calendar calendar;
    private TextView timeView;
    private TextView dateView;
    private TextView locationView;
    static final int DIALOG_DATE_ID = 0;
    private int currentUserId;
    private int userId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_stroll);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("userId", Context.MODE_PRIVATE);
        currentUserId = settings.getInt("ID", 0);
        final Bundle bundle = new Bundle();
        userId = bundle.getInt("USER_ID", 0);

        dateView = (TextView) findViewById(R.id.dateField);
        timeView = (TextView) findViewById(R.id.timeField);
        locationView = (TextView) findViewById(R.id.fieldLocation);
        setDateButton = (Button)findViewById(R.id.dateButton);
        setTimeButton = (Button)findViewById(R.id.timeButton);
        setLocationButton = (Button)findViewById(R.id.locationButton);
        strollButton = (Button)findViewById(R.id.buttonStroll);
        locationView.setText("Warsaw");

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_ID);
            }
        });

        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MakeStrollActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String str = String.format("%02d:%02d", hourOfDay, minute);
                                timeView.setText(str);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }

        });

        strollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeStroll();
            }
        });
    }

    public void showDialog(){
        showDialog(DIALOG_DATE_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if (id == DIALOG_DATE_ID){
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth){
            year = year1;
            month = monthOfYear + 1;
            day = dayOfMonth;
            String str = String.format("%02d.%02d.%d", day, month, year);
            dateView.setText(str);
        }
    };

    public void makeStroll(){
        String url ="http://10.0.2.2:8080/MakeStrollAndroid";
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        MediaType mediaType = MediaType.parse("application/json");
        StrollContent requestContent = new StrollContent(currentUserId, userId, dateView.getText().toString(), timeView.getText().toString(), locationView.getText().toString());
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(requestContent));

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
                Log.e("error", "error while connecting with server, try again");
                backgroundThreadShortToast(MakeStrollActivity.this, "Error in making stroll occured");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson retGson = new Gson();
                String jsonResponse = response.body().toString();
                MakeStrollResponse makeStrollResponse;
                if(response.body().contentLength() != 0) {
                    try {
                        makeStrollResponse = retGson.fromJson(jsonResponse, MakeStrollResponse.class);
                        backgroundThreadShortToast(MakeStrollActivity.this, makeStrollResponse.getResponseContent());
                        finish();

                    } catch (JsonSyntaxException e) {
                        Log.e("error", "error in syntax in returning json");
                    }
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
}
