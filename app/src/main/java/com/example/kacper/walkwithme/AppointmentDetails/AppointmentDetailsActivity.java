package com.example.kacper.walkwithme.AppointmentDetails;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.List;

public class AppointmentDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

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
