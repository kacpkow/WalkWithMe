package com.example.kacper.walkwithme.MainActivity.Announcements.EditAnnouncement;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
public class EditAnnouncementFragment extends Fragment {

    ImageButton editLocationButton;
    ImageButton editDescriptionButton;
    ImageButton editStartTimeButton;
    ImageButton editEndTimeButton;
    Button cancelDetailsButton;
    Button changeDetailsButton;

    Spinner privacySpinner;
    String privacy;

    TextView strollLocationView;
    TextView strollStartTimeView;
    TextView strollEndTimeView;
    TextView strollDescriptionView;

    OkHttpClient client;

    private Calendar calendar;
    private int year, month, day, hour, minute;

    private Double latitudeSet = 0.0;
    private Double longitudeSet = 0.0;
    Button searchLocation;

    public EditAnnouncementFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit_announcement, container, false);
        client = RequestController.getInstance().getClient();
        editStartTimeButton = (ImageButton)v.findViewById(R.id.strollStartTimeButton);
        editEndTimeButton = (ImageButton)v.findViewById(R.id.strollEndTimeButton);
        editLocationButton = (ImageButton)v.findViewById(R.id.strollLocationButton);
        editDescriptionButton = (ImageButton)v.findViewById(R.id.strollDescriptionButton);
        cancelDetailsButton = (Button)v.findViewById(R.id.cancelStrollDetails);
        changeDetailsButton = (Button)v.findViewById(R.id.changeStrollDetails);

        privacySpinner = (Spinner)v.findViewById(R.id.announcementPrivacySpinner);

        strollStartTimeView = (TextView) v.findViewById(R.id.strollStartTime);
        strollEndTimeView = (TextView) v.findViewById(R.id.strollEndTime);
        strollLocationView = (TextView) v.findViewById(R.id.strollLocation);
        strollDescriptionView = (TextView) v.findViewById(R.id.strollDescription);

        strollStartTimeView.setText(getArguments().getString("startTime"));
        strollEndTimeView.setText(getArguments().getString("endTime"));
        strollLocationView.setText(getArguments().getString("location"));
        strollDescriptionView.setText(getArguments().getString("description"));
        if(getArguments().getString("privacy").equals("Friends")){
            privacySpinner.setSelection(1);
        }
        else if(getArguments().getString("privacy").equals("hide")){
            privacySpinner.setSelection(2);
        }
        else{
            privacySpinner.setSelection(0);
        }

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        latitudeSet = getArguments().getDouble("latitude");
        longitudeSet = getArguments().getDouble("longtitude");

        editStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(strollStartTimeView);
            }
        });

        editEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(strollEndTimeView);
            }
        });

        editDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDescription(getArguments().getString("description"), v);
            }
        });

        editLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();
            }
        });

        cancelDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        getContext());

                dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setTitle("Do you want to quit from this edition?");
                alertDialog.show();
            }
        });

        changeDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        getContext());

                dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeDetails();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });


                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setTitle("Do you want to accept your changes?");
                alertDialog.show();
            }
        });

        privacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    privacy = "All";
                }
                else if(position == 1){
                    privacy = "Friends";
                }
                else {
                    privacy = "hide";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    public void changeDetails(){
        AdvertisementData changedAdvertisementData = new AdvertisementData();
        changedAdvertisementData.setStrollStartTime(strollStartTimeView.getText().toString());
        changedAdvertisementData.setStrollEndTime(strollEndTimeView.getText().toString());
        changedAdvertisementData.setAdEndTime(strollEndTimeView.getText().toString());
        changedAdvertisementData.setUserId(getArguments().getInt("userId"));
        changedAdvertisementData.setAdId(getArguments().getInt("adId"));
        changedAdvertisementData.setPrivacy(privacy);
        changedAdvertisementData.setDescription(strollDescriptionView.getText().toString());
        LocationData locationData = new LocationData();
        locationData.setDescription(strollLocationView.getText().toString());

        //TO DO
        //DO ZMIANY!
        locationData.setLocation_id(getArguments().getInt("locationId"));

        locationData.setLatitude(latitudeSet);
        locationData.setLongtitude(longitudeSet);

        changedAdvertisementData.setLocation(locationData);

        String url = getString(R.string.service_address) + "adv";
        Gson gson = new Gson();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(changedAdvertisementData));

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
                Log.e("error", "error while connectinh with server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson retGson = new Gson();
                String jsonResponse = response.body().string();

                if(jsonResponse != null){
                    Log.e("error", jsonResponse);
                }

            }
        });

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

    public void setDescription(String oldDescription, View view){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_field_dialog, null);
        final EditText editText = (EditText)dialogView.findViewById(R.id.editText);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton("SET DESCRIPTION", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                strollDescriptionView.setText(editText.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

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
                //Toast.makeText(MakeStrollActivity.this, "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MakeStrollActivity.this, "You cancelled", Toast.LENGTH_SHORT).show();
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
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
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

                            String locationSet = str.toString();
                            strollLocationView.setText(locationSet);

                        }
                    }
                });

            }});

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Float lat = latitudeSet.floatValue();
                Float lng = longitudeSet.floatValue();
                LatLng coordinates = new LatLng(lat, lng); ////your lat lng
                googleMap.addMarker(new MarkerOptions().position(coordinates).title("Marker"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
            }
        });

    }

}

