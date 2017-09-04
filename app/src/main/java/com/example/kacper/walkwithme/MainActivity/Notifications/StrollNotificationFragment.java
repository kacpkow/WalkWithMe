package com.example.kacper.walkwithme.MainActivity.Notifications;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kacper.walkwithme.Model.AdvertisementData;
import com.example.kacper.walkwithme.Model.StrollData;
import com.example.kacper.walkwithme.Model.UserProfileData;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class StrollNotificationFragment extends Fragment implements OnMapReadyCallback{

    Integer strollId;
    Integer userId;
    String location;

    AdvertisementData advData;

    TextView senderName;
    TextView strollStartTimeView;
    TextView strollEndTimeView;
    TextView locationView;
    ImageView personPhoto;

    Button cancelButton;
    Button acceptStrollButton;


    private GoogleMap mMap;
    private Marker marker;
    float latitude;
    float longtitude;
    private MapView mapView;

    OkHttpClient client;


    public StrollNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stroll_notification, container, false);

        senderName = (TextView)v.findViewById(R.id.senderNameField);
        strollStartTimeView = (TextView)v.findViewById(R.id.strollStartTimeField);
        strollEndTimeView = (TextView)v.findViewById(R.id.strollEndTimeField);
        locationView = (TextView)v.findViewById(R.id.strollLocationField);
        personPhoto = (ImageView)v.findViewById(R.id.strollIcon);
        cancelButton = (Button)v.findViewById(R.id.cancelButton);
        acceptStrollButton = (Button)v.findViewById(R.id.acceptStrollButton);

        userId = getArguments().getInt("senderId");
        strollId = getArguments().getInt("strollId");

        client = RequestController.getInstance().getClient();

        initializeSenderData();
        initializeStrollData();

        try{
            mapView = (MapView)v.findViewById(R.id.mapDetailsAppointment);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }catch (Exception ex){
            Log.d("exception", "map exception");
        }


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        acceptStrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        getContext());

                dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        makeStroll();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setTitle("Do you want to accept this stroll?");
                alertDialog.show();
            }
        });

        return v;
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
            Geocoder geocoder = new Geocoder(getContext());
            try {
                //addressList = geocoder.getFromLocationName(location, 1);
                addressList = geocoder.getFromLocation(latitude, longtitude, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addressList.size() != 0){
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                latitude = (float)address.getLatitude();
                longtitude = (float)address.getLongitude();
            }

        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    public void updateMap(){
        if (marker != null){
            mMap.clear();
        }

        List<Address> addressList = new ArrayList<>();
        if(location != null && !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(getContext());
            try {
                addressList = geocoder.getFromLocationName(location, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            if(!addressList.isEmpty()){
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
                if(mMap!= null){

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    latitude = (float)address.getLatitude();
                    longtitude = (float)address.getLongitude();

                }
            }

        }
    }

    public void initializeSenderData(){

            String url ="http://10.0.2.2:8080/user/"+ toString().valueOf(userId);
        Log.e("url", url);

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
                    Gson retGson = new Gson();
                    String json = response.body().string();
                    Log.e("json", json);

                    try{

                        UserProfileData usr = retGson.fromJson(json, UserProfileData.class);
                        backgroundThreadInitializeSender(getContext(), usr);


                    }catch (JsonSyntaxException e){
                        Log.e("exception", e.getLocalizedMessage());
                    }


                }
            });

    }

    public void initializeStrollData(){

        String url ="http://10.0.2.2:8080/adv/"+ toString().valueOf(strollId);

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
                Gson retGson = new Gson();
                String json = response.body().string();

                try{

                    AdvertisementData advertisementData = retGson.fromJson(json, AdvertisementData.class);
                    backgroundThreadInitializeStroll(getContext(), advertisementData);


                }catch (JsonSyntaxException e){
                    Log.e("exception", e.getLocalizedMessage());
                }


            }
        });

    }

    public void makeStroll(){

        String url ="http://10.0.2.2:8080/stroll/makeStroll";

        Gson gson = new Gson();

        MediaType mediaType = MediaType.parse("application/json");

        SharedPreferences settings = getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        int[] arr = {advData.getUserId(), settings.getInt("ID", 0)};
        arr[0] = advData.getUserId();

        StrollData strollData = new StrollData();

        strollData.setData_end(advData.getStrollEndTime());
        strollData.setData_start(advData.getStrollStartTime());
        strollData.setInfo(advData.getDescription());
        if(advData.getPrivacy().equals("Friends")){
            strollData.setPrivacy("Fri");
        }
        else if(advData.getPrivacy().equals("Hide")){
            strollData.setPrivacy("Hid");
        }
        else{
            strollData.setPrivacy("All");
        }
        strollData.setLocation(advData.getLocation());
        strollData.setStrollId(0);
        strollData.setUsers(arr);
        strollData.setStatus("activ");

        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(strollData));

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
                Log.e("response make stroll", response.body().string());
            }
        });

    }

    public void backgroundThreadInitializeSender(final Context context,
                                                 final UserProfileData user) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    senderName.setText(senderName.getText().toString() + " " + user.getFirstName() + " " +user.getLastName());
                    Glide.with(getContext()).load(user.getPhoto_url())
                            .into(personPhoto);
                }
            });
        }
    }

    public void backgroundThreadInitializeStroll(final Context context,
                                                 final AdvertisementData advertisementData) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    advData = advertisementData;
                    Log.e("adv id", advData.getUserId().toString());
                    strollStartTimeView.setText(strollStartTimeView.getText().toString() + " " + advData.getStrollStartTime());
                    strollEndTimeView.setText(strollEndTimeView.getText().toString() + " " + advData.getStrollEndTime());
                    locationView.setText(locationView.getText().toString()+" " + advData.getLocation().getDescription());
                    location = advData.getLocation().getDescription();
                    try {
                        updateMap();
                    }catch (Exception ex){
                        Log.d("err", "err");
                    }

                }
            });
        }
    }

}

