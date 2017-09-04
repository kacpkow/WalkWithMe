package com.example.kacper.walkwithme.MakeStrollActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kacper.walkwithme.Model.AdvertisementData;
import com.example.kacper.walkwithme.Model.LocationData;
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
public class MakeStrollFragment extends Fragment {

    Button setStrollStartButton;
    Button setStrollEndButton;
    Button setLocationButton;
    Button strollButton;
    Button searchLocation;

    private int year, month, day, hour, minute;
    private Calendar calendar;
    private TextView strollStartTime;
    private TextView strollEndTime;
    private TextView locationView;
    static final int DIALOG_DATE_ID = 0;
    private int currentUserId;
    private int userId;
    private String locationSet;
    private String userName;
    private Double latitudeSet = 0.0;
    private Double longitudeSet = 0.0;

    OkHttpClient client;


    public MakeStrollFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_make_stroll, container, false);

        client = RequestController.getInstance().getClient();

        SharedPreferences settings = getContext().getSharedPreferences("userId", Context.MODE_PRIVATE);
        currentUserId = settings.getInt("ID", 0);
        userId = getArguments().getInt("USER_ID", 0);
        userName = getArguments().getString("USER_NAME");

        strollStartTime= (TextView) v.findViewById(R.id.makeStrollStartTimeField);
        strollEndTime= (TextView) v.findViewById(R.id.makeStrollEndTImeField);
        locationView = (TextView) v.findViewById(R.id.fieldLocation);
        setStrollStartButton = (Button)v.findViewById(R.id.startTimeButton);
        setStrollEndButton = (Button)v.findViewById(R.id.strollEndTimeButton);
        setLocationButton = (Button)v.findViewById(R.id.locationButton);
        strollButton = (Button)v.findViewById(R.id.buttonStroll);
        calendar = Calendar.getInstance();

        strollEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(strollEndTime);
            }
        });

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);


        strollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        getContext());

                dialogBuilder.setTitle("Are you sure that you want to make a stroll with " + userName + "?");

                dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        makeStroll();
                        getFragmentManager().popBackStack();

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

        setStrollStartButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setDate(strollStartTime);
            }
        });

        setStrollEndButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setDate(strollEndTime);
            }
        });

        setLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();

            }
        });

        return v;
    }

    public void setTime(final TextView textView){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String str = String.format("%02d:%02d:00", hourOfDay, minute);
                        textView.setText(textView.getText() + " "+ str);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDate(final TextView textView){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth) {
                year = year1;
                month = monthOfYear;
                day = dayOfMonth;
                String str = String.format("%02d-%02d-%d", year, month + 1, day);
                textView.setText(str);
                setTime(textView);
            }
        }, year, month, day);

        datePickerDialog.show();
    }



    public void makeStroll(){

        String url ="http://10.0.2.2:8080/adv/" + toString().valueOf(userId);
        Gson gson = new Gson();
        MediaType mediaType = MediaType.parse("application/json");
        AdvertisementData requestContent = new AdvertisementData();
        LocationData locationData = new LocationData();

        locationData.setLocation_id(0);
        locationData.setLatitude(latitudeSet);
        locationData.setLongtitude(longitudeSet);
        locationData.setDescription(locationSet);

        requestContent.setLocation(locationData);
        requestContent.setDescription("");
        requestContent.setAdId(0);
        requestContent.setUserId(currentUserId);
        requestContent.setStrollStartTime(strollStartTime.getText().toString());
        requestContent.setStrollEndTime(strollEndTime.getText().toString());
        requestContent.setAdEndTime(strollStartTime.getText().toString());

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
                backgroundThreadShortToast(getContext(), "Error in making stroll occured");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson retGson = new Gson();
                String jsonResponse = response.body().string();
                if(response.code() == 201) {
                    try {
                        backgroundThreadShortToast(getContext(), "The stroll propose has been sent");

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
                getContext());
        final GoogleMap googleMap;
        final EditText[] location_tf = new EditText[1];

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_map, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton("SET LOCATION", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        MapView mMapView = (MapView) alertDialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(getContext());

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
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                        String location = location_tf[0].getText().toString();
                        List<Address> addressList = null;
                        if(location != null && !location.equals(""))
                        {
                            Geocoder geocoder = new Geocoder(getContext());
                            try {
                                addressList = geocoder.getFromLocationName(location, 1);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if(addressList != null){
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
                    }
                });

            }});

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                SharedPreferences settings = getActivity().getSharedPreferences("userLocation", Context.MODE_PRIVATE);
                Float lat = settings.getFloat("latitude", 0.0f);
                Float lng = settings.getFloat("longtitude", 0.0f);
                LatLng coordinates = new LatLng(lat, lng); //your lat lng
                googleMap.addMarker(new MarkerOptions().position(coordinates).title("Marker"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
            }
        });

    }

}
