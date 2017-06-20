package com.example.kacper.walkwithme.AppointmentDetails;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kacper.walkwithme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AppointmentDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    Integer strollId;
    Integer userId;
    String firstName;
    String lastName;
    String location;
    String date;
    String time;
    String mediumPhoto;

    TextView dateView;
    TextView timeView;
    TextView locationView;
    ImageView personPhoto;

    Button cancelStrollButton;
    Button editStrollDetailsButton;


    private GoogleMap mMap;
    private Marker marker;
    float latitude;
    float longtitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        Bundle b = getIntent().getExtras();

        dateView = (TextView)findViewById(R.id.appointmentDateField);
        timeView = (TextView)findViewById(R.id.appointmentTimeField);
        locationView = (TextView)findViewById(R.id.appointmentLocationField);
        personPhoto = (ImageView)findViewById(R.id.appointmentIcon);
        cancelStrollButton = (Button)findViewById(R.id.cancelStrollButton);
        editStrollDetailsButton = (Button)findViewById(R.id.editStrollButton);

        strollId = b.getInt("STROLL_ID");
        userId = b.getInt("USER_ID");
        firstName = b.getString("USER_FIRST_NAME");
        lastName = b.getString("USER_LAST_NAME");
        mediumPhoto = b.getString("USER_IMAGE");
        date = b.getString("DATE");
        time = b.getString("TIME");
        location = b.getString("LOCATION");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDetailsAppointment);
        mapFragment.getMapAsync(this);

        dateView.setText(dateView.getText().toString() + date);
        timeView.setText(timeView.getText().toString() + time);
        locationView.setText(locationView.getText().toString() + location);
        Glide.with(this).load(mediumPhoto).into(personPhoto);

        cancelStrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        AppointmentDetailsActivity.this);

                dialogBuilder.setTitle("Are you sure that you want to cancel this stroll?");

                dialogBuilder.setPositiveButton("CANCEL STROLL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(AppointmentDetailsActivity.this, "You cancelled a stroll", Toast.LENGTH_SHORT).show();
                        if(cancelStroll(strollId) == true){
                            finish();
                        }
                        else{
                            Toast.makeText(AppointmentDetailsActivity.this, "An error occured, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AppointmentDetailsActivity.this, "You click no", Toast.LENGTH_SHORT).show();
                    }
                });

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

            }
        });

    }

    public boolean cancelStroll(Integer strollId){
        String url ="http://10.0.2.2:8080/CancelStrollAndroid";
        OkHttpClient client = new OkHttpClient();
        final boolean[] flag = {false};
        Gson gson = new Gson();

        Integer id = strollId;
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(id));

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

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Gson retGson = new Gson();
                int code = response.code();
                if(code == 500){
                    flag[0] = true;
                }
            }
        });

        if(flag[0] == true)
            return true;

        return false;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (marker != null){
            mMap.clear();
        }
        mMap = googleMap;
        List<Address> addressList = null;
        if(location != null && !location.equals(""))
        {
            Toast.makeText(this, location, Toast.LENGTH_SHORT).show();
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            latitude = (float)address.getLatitude();
            longtitude = (float)address.getLongitude();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);


    }
}
