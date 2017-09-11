package com.example.kacper.walkwithme.MainActivity.Announcements.AnnouncementDetails;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kacper.walkwithme.Model.AdvertisementData;
import com.example.kacper.walkwithme.Model.LocationData;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.google.android.gms.common.api.GoogleApiClient;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementDetailsFragment extends Fragment {

    TextView strollLocationView;
    TextView strollStartTimeView;
    TextView strollEndTimeView;
    TextView strollDescriptionView;
    ImageButton showLocationButton;
    Button cancelButton;
    Button takePartInStroll;

    String locationName;
    Integer userId;
    String description;
    Double latitude;
    Double longitude;
    Integer myId;
    String advEndTime;
    String startTime;
    String endTime;

    OkHttpClient client;

    public AnnouncementDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_announcement_details, container, false);
        client = RequestController.getInstance().getClient();

        showLocationButton = (ImageButton)v.findViewById(R.id.strollLocationButton);
        cancelButton = (Button)v.findViewById(R.id.cancelDetails);
        takePartInStroll = (Button)v.findViewById(R.id.takePartInStrollButton);

        strollStartTimeView = (TextView) v.findViewById(R.id.strollStartTime);
        strollEndTimeView = (TextView) v.findViewById(R.id.strollEndTime);
        strollLocationView = (TextView) v.findViewById(R.id.strollLocation);
        strollDescriptionView = (TextView) v.findViewById(R.id.strollDescription);

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("USER_ID", Context.MODE_PRIVATE);
        myId = settings.getInt("userId", 0);

        locationName = getArguments().getString("location");
        description = getArguments().getString("description");
        userId = getArguments().getInt("userId", 0);
        latitude = getArguments().getDouble("latitude");
        longitude = getArguments().getDouble("longtitude");
        advEndTime = getArguments().getString("advEndTime");
        startTime = getArguments().getString("startTime");
        endTime = getArguments().getString("endTime");

        strollStartTimeView.setText(startTime);
        strollEndTimeView.setText(endTime);

        strollLocationView.setText(locationName);
        strollDescriptionView.setText(description);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        showLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();
            }
        });

        takePartInStroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePartInStroll();
            }
        });

        return v;
    }


    public void takePartInStroll(){
        String url = getString(R.string.service_address) + "adv/" + toString().valueOf(userId);
        Gson gson = new Gson();
        MediaType mediaType = MediaType.parse("application/json");
        AdvertisementData requestContent = new AdvertisementData();
        LocationData locationData = new LocationData();

        locationData.setLocation_id(0);
        locationData.setLatitude(latitude);
        locationData.setLongtitude(longitude);
        locationData.setDescription(locationName);

        requestContent.setLocation(locationData);
        requestContent.setDescription(description);
        requestContent.setAdId(0);
        requestContent.setUserId(myId);
        requestContent.setStrollStartTime(startTime);
        requestContent.setStrollEndTime(endTime);
        requestContent.setAdEndTime(advEndTime);

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
                backgroundThreadShortToast(getContext(), "Error in making stroll occured");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 201) {
                    try {
                        backgroundThreadShortToast(getContext(), "The stroll invitation has been sent");

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
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
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(
                getContext());
        final GoogleMap googleMap;

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_map_announcement_details, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.show();

        MapView mMapView = (MapView) alertDialog.findViewById(R.id.mapViewAnnouncementDetails);
        MapsInitializer.initialize(getContext());

        mMapView.onCreate(alertDialog.onSaveInstanceState());
        mMapView.onResume();

        final MapView finalMMapView = mMapView;

        finalMMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                List<Address> addressList = null;
                try {
                    addressList = geocoder.getFromLocationName(locationName, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(addressList != null){
                    if(addressList.size() != 0){
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                    }
                }

            }
        });
    }

}
