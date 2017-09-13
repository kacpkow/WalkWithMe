package com.example.kacper.walkwithme.MainActivity.Announcements.AnnouncementDetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kacper.walkwithme.AppointmentDetails.ParticipantsAdapter;
import com.example.kacper.walkwithme.Model.AdvertisementData;
import com.example.kacper.walkwithme.Model.LocationData;
import com.example.kacper.walkwithme.Model.UserProfileData;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
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
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */
public class AnnouncementDetailsFragment extends Fragment {

    TextView strollLocationView;
    TextView strollStartTimeView;
    TextView strollEndTimeView;
    TextView strollDescriptionView;
    ImageButton showLocationButton;
    Button cancelButton;
    Button takePartInStroll;
    Button showPerson;
    private RecyclerView rv;

    android.app.AlertDialog dialog;

    List<UserProfileData> personsList;

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
        showPerson = (Button)v.findViewById(R.id.showPerson);

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

        if(myId == userId){
            takePartInStroll.setEnabled(false);
            takePartInStroll.setBackgroundResource(R.drawable.button_search_inactive);
        }

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

        showPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPerson();
            }
        });

        return v;
    }

    public void showPerson(){
       getUserData();
    }

    public void getUserData(){
        String url = getString(R.string.service_address)+"user/"+String.valueOf(userId);

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
                if(response.code() == 200){
                    UserProfileData usr = retGson.fromJson(json, UserProfileData.class);
                    backgroundThreadShowDialog(getContext(), usr);
                }
            }
        });
    }

    public void backgroundThreadShowDialog(final Context context,
                                                     final UserProfileData usr) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    showDialog(usr);
                }
            });
        }
    }

    public void showDialog(UserProfileData user){
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_stroll_participants, null);
        dialogBuilder.setView(dialogView);

        rv=(RecyclerView)dialogView.findViewById(R.id.rvParticipants);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        personsList = new ArrayList<>();
        Log.e("User", String.valueOf(user.getUser_id()));
        personsList.add(user);

        ParticipantsAdapter adapter = new ParticipantsAdapter(personsList, this.getContext());

        rv.setAdapter(adapter);

        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MakeStrollActivity.this, "You cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        dialog = dialogBuilder.create();
        dialog.show();
    }


    public void takePartInStroll(){
        String url = getString(R.string.service_address) + "adv/" + toString().valueOf(userId);
        Log.e("url", url);
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
        Log.e("jsoncontent", gson.toJson(requestContent));

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

    @Override
    public void onPause(){
        if(dialog!=null){
            dialog.dismiss();
        }
        super.onPause();
    }

}
