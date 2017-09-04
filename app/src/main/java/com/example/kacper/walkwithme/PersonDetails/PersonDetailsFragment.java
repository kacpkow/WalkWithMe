package com.example.kacper.walkwithme.PersonDetails;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kacper.walkwithme.MakeStrollActivity.MakeStrollFragment;
import com.example.kacper.walkwithme.Model.UserProfileData;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonDetailsFragment extends Fragment {

    private TextView nick;
    private TextView name;
    private TextView age;
    private TextView description;
    private TextView location;
    private ImageView image;
    private Button strollButton;
    private Button addToFriendsButton;
    private Integer userId;
    private String userAge;
    private String userLocation;
    private String userDescription;
    private Integer userId1;
    private String userName;
    private String userImageUrl;

    OkHttpClient client;

    public PersonDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        client = RequestController.getInstance().getClient();
        View v = inflater.inflate(R.layout.fragment_person_details2, container, false);

        nick = (TextView)v.findViewById(R.id.userNick);
        name = (TextView)v.findViewById(R.id.userName);
        age = (TextView)v.findViewById(R.id.userAge);
        description = (TextView)v.findViewById(R.id.userDescription);
        location = (TextView)v.findViewById(R.id.userLocation);
        image = (ImageView)v.findViewById(R.id.photo);
        strollButton = (Button)v.findViewById(R.id.strollButton);
        addToFriendsButton = (Button)v.findViewById(R.id.addToFriendsButton);

        userId = getArguments().getInt("USER_ID", 0);
        Log.e("usrid", toString().valueOf(userId));

        if(userId != 0){
            userId = getArguments().getInt("USER_ID");
            userAge = getArguments().getString("USER_AGE");
            userLocation = getArguments().getString("USER_LOCATION");
            userDescription= getArguments().getString("USER_DESCRIPTION");
            userName = getArguments().getString("USER_NAME");
            userImageUrl = getArguments().getString("USER_IMAGE");
            setParameters();
        }
        else{
            userId1 = getArguments().getInt("USER_ID_1", 0);
            getPerson();
        }

        strollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MakeStrollFragment newFragment = new MakeStrollFragment();
                Fragment f = ((AppCompatActivity)v.getContext()).getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                Bundle args = new Bundle();
                args.putInt("USER_ID", userId);
                args.putString("USER_NAME", userName);
                newFragment.setArguments(args);

                ft.replace(R.id.fragment_container, newFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        addToFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        getContext());

                dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inviteToFriends();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setTitle("Do you want to invite " + userName +" to friends?" );
                alertDialog.show();
            }
        });

        return v;
    }

    public void setParameters(){

        nick.setText(userId.toString());
        name.setText(userName);
        age.setText(userAge);
        location.setText(userLocation);
        description.setText(userDescription);

        Glide.with(this).load(userImageUrl)
                .into(image);

    }

    public void inviteToFriends(){

        String url ="http://10.0.2.2:8080/friends/invite/" +toString().valueOf(userId);
        Gson gson = new Gson();
        MediaType mediaType = MediaType.parse("application/json");

        String str = null;
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(str));

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
                Log.e("error", "error while connectinh with server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson retGson = new Gson();
                String jsonResponse = response.body().string();

                Log.e("respCode: ", String.valueOf(response.code()));

                if(jsonResponse != null){
                    Log.e("error", jsonResponse);
                }

            }
        });
    }

    public void getPerson(){

        String url ="http://10.0.2.2:8080/user/" +toString().valueOf(userId1);
//        OkHttpClient client = new OkHttpClient();
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
                Log.e("error", "error while connectinh with server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson retGson = new Gson();
                String jsonResponse = response.body().string();

                if(jsonResponse != null){
                    Log.e("json", jsonResponse);
                    UserProfileData usrProfileData = retGson.fromJson(jsonResponse, UserProfileData.class);
                    userId = userId1;

                    String year = usrProfileData.getBirth_date().substring(0, Math.min(usrProfileData.getBirth_date().length(), 4));
                    String month = usrProfileData.getBirth_date().substring(5, Math.min(usrProfileData.getBirth_date().length(), 7));
                    String day = usrProfileData.getBirth_date().substring(9, Math.min(usrProfileData.getBirth_date().length(), 11));

                    Calendar c = Calendar.getInstance();
                    int actualYear = c.get(Calendar.YEAR);
                    int actualMonth = c.get(Calendar.MONTH);
                    int actualDay = c.get(Calendar.DAY_OF_MONTH);

                    Double calcAge =Double.valueOf(year) + Double.valueOf(month)/100.0 + Double.valueOf(day)/10000.0;
                    Double actCalcAge = actualYear + actualMonth/100.0 + actualDay/10000.0;
                    Double calc = Math.floor(actCalcAge - calcAge);

                    userAge = toString().valueOf(calc.intValue());
                    userLocation = usrProfileData.getCity();
                    userDescription= usrProfileData.getDescription();
                    userName = usrProfileData.getFirstName() +" "+usrProfileData.getLastName();
                    userImageUrl = usrProfileData.getPhoto_url();

                    backgroundThreadSetParameters(getContext());
                }

            }
        });
    }

    public void backgroundThreadSetParameters(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    setParameters();
                }
            });
        }
    }

}
