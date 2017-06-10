package com.example.kacper.walkwithme.MakeStrollActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kacper.walkwithme.MainActivity.PersonsList.Person;
import com.example.kacper.walkwithme.MainActivity.PersonsList.SearchContent;
import com.example.kacper.walkwithme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    Button searchLocation;
    private int year, month, day, hour, minute;
    private Calendar calendar;
    private TextView timeView;
    private TextView dateView;
    private TextView locationView;
    static final int DIALOG_DATE_ID = 0;
    private int currentUserId;
    private int userId;
    private String locationSet;
    private String userName;
    private Double latitudeSet = 0.0;
    private Double longitudeSet = 0.0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_stroll);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("userId", Context.MODE_PRIVATE);
        currentUserId = settings.getInt("ID", 0);
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("USER_ID", 0);
        userName = getIntent().getExtras().getString("USER_NAME");

        dateView = (TextView) findViewById(R.id.dateField);
        timeView = (TextView) findViewById(R.id.timeField);
        locationView = (TextView) findViewById(R.id.fieldLocation);
        setDateButton = (Button)findViewById(R.id.dateButton);
        setTimeButton = (Button)findViewById(R.id.timeButton);
        setLocationButton = (Button)findViewById(R.id.locationButton);
        strollButton = (Button)findViewById(R.id.buttonStroll);

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

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        MakeStrollActivity.this);

                dialogBuilder.setTitle("Are you sure that you want to make a stroll with " + userName + "?");

                dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(AppointmentDetailsActivity.this, "You cancelled a stroll", Toast.LENGTH_SHORT).show();
                        makeStroll();
                        finish();
                    }
                });
                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        setLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();

            }
        });

        Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();
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

        String url ="http://10.0.2.2:8080/makeStroll";
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        MediaType mediaType = MediaType.parse("application/json");
        StrollContent requestContent = new StrollContent(currentUserId, userId, dateView.getText().toString(), timeView.getText().toString(), locationView.getText().toString(), longitudeSet, latitudeSet);
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
                String jsonResponse = response.body().string();
                if(response.code() == 200) {
                    try {
                        backgroundThreadShortToast(MakeStrollActivity.this, "The stroll propose has been sent");

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

    public void showMap(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                MakeStrollActivity.this);
        final GoogleMap googleMap;
        final EditText[] location_tf = new EditText[1];

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_map, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton("SET LOCATION", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MakeStrollActivity.this, "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MakeStrollActivity.this, "You cancelled", Toast.LENGTH_SHORT).show();
            }
        });


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        MapView mMapView = (MapView) alertDialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(MakeStrollActivity.this);

        mMapView.onCreate(alertDialog.onSaveInstanceState());
        mMapView.onResume();

        mMapView = (MapView) alertDialog.findViewById(R.id.mapView);
        mMapView.onCreate(alertDialog.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately

        searchLocation = (Button)alertDialog.findViewById(R.id.Bsearch);
        final MapView finalMMapView = mMapView;
        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_tf[0] = (EditText)alertDialog.findViewById(R.id.TFaddress);
                finalMMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                        String location = location_tf[0].getText().toString();
                        List<Address> addressList = null;
                        if(location != null && !location.equals(""))
                        {
                            Geocoder geocoder = new Geocoder(MakeStrollActivity.this);
                            try {
                                addressList = geocoder.getFromLocationName(location, 1);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Address address = addressList.get(0);
                            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
                            latitudeSet = address.getLatitude();
                            longitudeSet = address.getLongitude();

                            googleMap.clear();
                            googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            StringBuilder str = new StringBuilder();
                            int maxAddressLineIndex = address.getMaxAddressLineIndex();
                            for(int i = 0; i <= maxAddressLineIndex; i++) {
                                if(i != maxAddressLineIndex){
                                    str.append(address.getAddressLine(i) + ", ");
                                }
                                else{
                                    str.append(address.getAddressLine(i));
                                }
                            }

                            locationSet = str.toString();
                            locationView.setText(locationSet);

                        }
                    }
                });

        }});

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                SharedPreferences settings = getSharedPreferences("userLocation", Context.MODE_PRIVATE);
                Float lat = settings.getFloat("latitude", 0.0f);
                Float lng = settings.getFloat("longtitude", 0.0f);
                LatLng coordinates = new LatLng(lat, lng); ////your lat lng
                googleMap.addMarker(new MarkerOptions().position(coordinates).title("Marker"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
            }
        });

    }
}
