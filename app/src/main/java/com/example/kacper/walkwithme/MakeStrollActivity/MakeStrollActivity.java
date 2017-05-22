package com.example.kacper.walkwithme.MakeStrollActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.kacper.walkwithme.R;

public class MakeStrollActivity extends AppCompatActivity {
    Button setDateButton;
    Button setTimeButton;
    private int year, month, day, hour, minute;
    private Calendar calendar;
    private TextView timeView;
    private TextView dateView;
    static final int DIALOG_DATE_ID = 0;
    static final int DIALOG_TIME_ID = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_stroll);
        dateView = (TextView) findViewById(R.id.dateField);
        timeView = (TextView) findViewById(R.id.timeField);
        setDateButton = (Button)findViewById(R.id.dateButton);
        setTimeButton = (Button)findViewById(R.id.timeButton);

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
}
