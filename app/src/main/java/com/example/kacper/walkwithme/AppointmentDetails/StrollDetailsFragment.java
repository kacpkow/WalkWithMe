package com.example.kacper.walkwithme.AppointmentDetails;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kacper.walkwithme.AppModel.ParticipantsData;
import com.example.kacper.walkwithme.Model.User;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class StrollDetailsFragment extends Fragment implements OnMapReadyCallback {

    Integer strollId;
    Integer userId;
    String location;
    String startTime;
    String endTime;
    String description;
    int[] participants;

    int participantsNumber = 0;

    List<ParticipantsData> participantsList;

    TextView startTimeView;
    TextView endTimeView;
    TextView locationView;
    TextView descriptionView;

    Button cancelStrollButton;
    Button showParticipatorsButton;

    ProgressDialog showParticipantsDialog;
    private GoogleMap mMap;
    private Marker marker;
    float latitude;
    float longtitude;

    private ListView listView;
    private ArrayAdapter<String> adapter ;

    OkHttpClient client;

    private MapView mapView;

    public StrollDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stroll_details, container, false);
        Bundle b = getActivity().getIntent().getExtras();
        client = RequestController.getInstance().getClient();

        startTimeView = (TextView)v.findViewById(R.id.strollStartTimeField);
        endTimeView = (TextView)v.findViewById(R.id.strollEndTimeField);
        locationView = (TextView)v.findViewById(R.id.strollLocationField);
        descriptionView = (TextView)v.findViewById(R.id.strollDescriptionField);
        cancelStrollButton = (Button)v.findViewById(R.id.cancelButton);
        showParticipatorsButton = (Button)v.findViewById(R.id.showParticipatorsButton);

        strollId = getArguments().getInt("STROLL_ID");
        startTime = getArguments().getString("startTime");
        endTime = getArguments().getString("endTime");
        location = getArguments().getString("location");
        description = getArguments().getString("description");
        participants = getArguments().getIntArray("participants");

        participantsList = new ArrayList<>();

        startTimeView.setText(startTimeView.getText().toString() + startTime);
        endTimeView.setText(endTimeView.getText().toString() + endTime);
        descriptionView.setText(descriptionView.getText().toString() + description);
        locationView.setText(locationView.getText().toString() + location);
        //Glide.with(this).load(mediumPhoto).into(personPhoto);

        cancelStrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        getContext());

                dialogBuilder.setTitle("Are you sure that you want to cancel your participation in this stroll?");

                dialogBuilder.setPositiveButton("CANCEL STROLL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(AppointmentDetailsActivity.this, "You cancelled a stroll", Toast.LENGTH_SHORT).show();
                        if(cancelStroll(strollId) == true){
                            Toast.makeText(getActivity().getApplicationContext(), "You cancelled stroll", Toast.LENGTH_SHORT).show();
                            getActivity().getFragmentManager().popBackStack();
                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "An error occured, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(), "You click no", Toast.LENGTH_SHORT).show();
                    }
                });

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

            }
        });

        showParticipatorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParticipantsDialog = new ProgressDialog(getContext());
                showParticipantsDialog .setTitle("Loading, please wait...");
                showParticipantsDialog.show();
                loadParticipants();

            }
        });

        try{
            mapView = (MapView)v.findViewById(R.id.mapDetailsForcomingAppointment);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }catch (Exception ex){
            Log.d("exception", "map exception");
        }

        return v;
    }

    public void loadParticipants(){
        if(participants != null){
            for(int singleParticipant: participants){
                Log.e("participant id", String.valueOf(singleParticipant));
                loadSingleParticipant(singleParticipant);
                participantsNumber++;
            }
        }
        else{
            showParticipantsDialog.dismiss();
            showParticipants();
        }

    }

    public void showParticipants(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_stroll_participants, null);
        dialogBuilder.setView(dialogView);

        listView = (ListView) dialogView.findViewById(R.id.listview);
        String cars[] = {"Mercedes", "Fiat", "Ferrari", "Aston Martin", "Lamborghini", "Skoda", "Volkswagen"};
        ArrayList<String> participantsL = new ArrayList<String>();
        for(ParticipantsData data: participantsList){
            participantsL.add(data.getName());
        }

        adapter = new ArrayAdapter<String>(getContext(), R.layout.participants_item, participantsL);

        listView.setAdapter(adapter);

        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MakeStrollActivity.this, "You cancelled", Toast.LENGTH_SHORT).show();
            }
        });


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void loadSingleParticipant(int singleParticipant){
        String url ="http://10.0.2.2:8080/user/"+String.valueOf(singleParticipant);

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
                    backgroundThreadAddParticipantToList(getContext(), usr);
                }
            }
        });
    }

    public void backgroundThreadAddParticipantToList(final Context context,
                                                     final UserProfileData usr) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    ParticipantsData newParticipant = new ParticipantsData(usr.getFirstName() + " " + usr.getLastName());
                    participantsList.add(newParticipant);
                    if(participantsNumber == participants.length){
                        showParticipantsDialog.dismiss();
                        showParticipants();
                    }
                }
            });
        }
    }

    public boolean cancelStroll(Integer strollId){
        String url ="http://10.0.2.2:8080/stroll/delete/"+strollId.toString();
        final boolean[] flag = {false};
        Gson gson = new Gson();

        Integer id = strollId;
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(id));

        final Request request;
        request = new Request.Builder()
                .url(url)
                .delete(requestBody)
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
                Log.e("was ok code", "Was ok");
                if(code == 200){
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
            Geocoder geocoder = new Geocoder(getContext());
            try {
                //addressList = geocoder.getFromLocationName(location, 1);
                addressList = geocoder.getFromLocationName(location, 1);

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
}
