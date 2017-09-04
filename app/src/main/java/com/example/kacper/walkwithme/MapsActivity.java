package com.example.kacper.walkwithme;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kacper.walkwithme.Model.LocationData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Marker marker;
    float latitude = 0.0f;
    float longtitude = 0.0f;
    String locationDescription;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Double lastLocationLat = 0.0;
    private Double lastLocationLng = 0.0;
    OkHttpClient client;

    float latitudeFromBase = 0.0f;
    float longtitudeFromBase = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        client = RequestController.getInstance().getClient();

        mapFragment.getMapAsync(this);

        getUserLocationData();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void getUserLocationData(){
        String url ="http://10.0.2.2:8080/profile/location";

        Gson gson = new Gson();

        MediaType mediaType = MediaType.parse("application/json");

        final Request request;
        request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String json;
                json = response.body().string();
                Gson retGson = new Gson();

                if(response.code() == 200){
                    LocationData location = retGson.fromJson(json, LocationData.class);
                    latitudeFromBase = (float)location.getLatitude();
                    longtitudeFromBase = (float)location.getLongtitude();
                    backgroundThreadSetLocation(getApplicationContext());
                }

            }
        });

    }

    public void onSearch(View view)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if(location != null && !location.equals(""))
        {
            Log.e("log", location_tf.getText().toString());
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
                Address address = (Address) addressList.get(0);
                locationDescription = "";
                locationDescription += address.getAddressLine(0) + ", ";
                locationDescription += address.getAddressLine(1) + ", ";
                locationDescription += address.getAddressLine(2);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            latitude = (float)address.getLatitude();
            longtitude = (float)address.getLongitude();
        }
    }

    public void onSave(View view){
        getApplicationContext();
        SharedPreferences settings = getSharedPreferences("userLocation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("latitude", latitude);
        editor.putFloat("longtitude", longtitude);
        editor.putString("description", locationDescription);
        editor.commit();
        Toast.makeText(this, String.valueOf(latitude), Toast.LENGTH_SHORT).show();
        finish();
        if(latitude != 0.0f && longtitude != 0.0f){
            saveLocationToBase();
        }
    }

    public void backgroundThreadSetLocation(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    setLocation();
                }
            });
        }
    }

    public void setLocation(){
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(latitudeFromBase, longtitudeFromBase, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("lat", String.valueOf(latitudeFromBase));
        Log.e("lng", String.valueOf(longtitudeFromBase));

        if(addressList.get(0) != null){
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            latitude = (float)address.getLatitude();
            longtitude = (float)address.getLongitude();
        }

    }

    public void saveLocationToBase(){
        String url ="http://10.0.2.2:8080/profile/location";

        Gson gson = new Gson();

        MediaType mediaType = MediaType.parse("application/json");

        LocationData location = new LocationData();
        location.setLongtitude(longtitude);
        location.setLatitude(latitude);
        location.setDescription(locationDescription);

        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(location));

        final Request request;
        request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String json;
                json = response.body().string();

                if(response.code() == 200){
                    Log.e("location", "location changed");
                }
                else{

                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       if (marker != null){
            mMap.clear();
        }
        mMap = googleMap;
        LatLng warsawLatLng = new LatLng(52.229675,21.012228);
        marker = mMap.addMarker(new MarkerOptions().position(warsawLatLng)
                .title("Warszawa"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(warsawLatLng, 15));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lastLocationLat = mLastLocation.getLatitude();
            lastLocationLng = mLastLocation.getLongitude();
        }

        if (marker != null){
            mMap.clear();
        }

        LatLng currentLatLng = new LatLng(lastLocationLat, lastLocationLng);
        marker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                .title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
