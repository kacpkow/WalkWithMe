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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kacper.walkwithme.MainActivity.Conversation.ConversationFragment;
import com.example.kacper.walkwithme.MakeStrollActivity.MakeStrollFragment;
import com.example.kacper.walkwithme.Model.UserProfileData;
import com.example.kacper.walkwithme.Model.UserProfileDataDeserializer;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
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
public class PersonDetailsFragment extends Fragment {

    private TextView nick;
    private TextView name;
    private TextView age;
    private TextView description;
    private TextView location;
    private ImageView image;
    private Button strollButton;
    private Button addToFriendsButton;
    private Button sendMessageButton;
    private Integer userId;
    private String userAge;
    private String userLocation;
    private String userDescription;
    private String userNick;
    private Integer userId1;
    private String userName;
    private String userImage;
    private TextView friendName;
    private TextView friendStatus;

    boolean friendState = false;

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
        friendName = (TextView)v.findViewById(R.id.personNameInFriend);
        friendStatus = (TextView)v.findViewById(R.id.personStatusInFriend);

        image = (ImageView)v.findViewById(R.id.photo);
        strollButton = (Button)v.findViewById(R.id.strollButton);
        addToFriendsButton = (Button)v.findViewById(R.id.addToFriendsButton);
        sendMessageButton = (Button)v.findViewById(R.id.sendMessageButton);

        userId = getArguments().getInt("USER_ID", 0);
        Log.e("id passed", String.valueOf(userId));

        if(userId != 0){
            getPerson(userId);
        }
        else{
            userId1 = getArguments().getInt("USER_ID_1", 0);
            getPerson(userId1);
        }

        isFriend();

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

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ConversationFragment newFragment = new ConversationFragment();
                Fragment f = ((AppCompatActivity)v.getContext()).getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                Bundle args = new Bundle();
                args.putInt("userId", userId);
                newFragment.setArguments(args);

                ft.replace(R.id.fragment_container, newFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return v;
    }

    public void getUserData(){

        String url = getString(R.string.service_address) + "friends";

        final Request request;
        request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", "error while connecting with server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResponse = response.body().string();

                Gson objGson = new GsonBuilder().setPrettyPrinting().create();
                Type listType = new TypeToken<List<UserProfileData>>() {
                }.getType();

                try {
                    List<UserProfileData> readFromJson = objGson.fromJson(jsonResponse, listType);

                    for (UserProfileData userProfileData:readFromJson
                            ) {
                        try {
                            if (userProfileData.getUser_id() == userId){
                                friendState = true;
                                break;
                            }

                        } catch (JsonSyntaxException e) {
                            Log.e("error", "error in syntax in returning json");
                        }
                    }
                }catch (JsonSyntaxException e){
                    Log.e("Json Syntax exception", e.getLocalizedMessage());
                }

                backgroundThreadInitializeFriend(getActivity().getApplicationContext(), friendState);
            }
        });

    }

    public void isFriend(){

        String url = getString(R.string.service_address) + "friends";

        final Request request;
        request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", "error while connecting with server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResponse = response.body().string();

                Gson objGson = new GsonBuilder().setPrettyPrinting().create();
                Type listType = new TypeToken<List<UserProfileData>>() {
                }.getType();

                try {
                    List<UserProfileData> readFromJson = objGson.fromJson(jsonResponse, listType);

                    if(readFromJson != null){
                        for (UserProfileData userProfileData:readFromJson
                                ) {
                            try {
                                Log.e("from fragment", String.valueOf(userProfileData.getUser_id()));
                                if (userProfileData.getUser_id() == userId){
                                    friendState = true;
                                    break;
                                }

                            } catch (JsonSyntaxException e) {
                                Log.e("error", "error in syntax in returning json");
                            }
                        }
                    }
                }catch (JsonSyntaxException e){
                    Log.e("Json Syntax exception", e.getLocalizedMessage());
                }

                backgroundThreadInitializeFriend(getActivity().getApplicationContext(), friendState);
            }
        });

    }

    public void setParameters(){

        nick.setText(userNick);
        name.setText(userName);
        age.setText(userAge);
        location.setText(userLocation);
        description.setText(userDescription);

        Glide.with(this)
                .load(Base64.decode(userImage, Base64.DEFAULT))
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(image);

    }

    public void inviteToFriends(){

        String url = getString(R.string.service_address)+"invite/" +toString().valueOf(userId);
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
                    isFriend();
                }

            }
        });
    }

    public void deleteFriend(){

        String url = getString(R.string.service_address)+"friends/"+String.valueOf(userId);

        final Request request;
        request = new Request.Builder()
                .url(url)
                .delete()
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

                if(response.code() == 200){
                    friendState = false;
                    isFriend();
                }

            }
        });
    }

    public void getPerson(Integer usrId){

        String url = getString(R.string.service_address) + "user/" +toString().valueOf(usrId);

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
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(UserProfileData.class, new UserProfileDataDeserializer());
                final Gson retGson = gsonBuilder.create();
                String jsonResponse = response.body().string();

                if(jsonResponse != null){
                    Log.e("json", jsonResponse);
                    UserProfileData usrProfileData = retGson.fromJson(jsonResponse, UserProfileData.class);
                    if(userId1 != null){
                        userId = userId1;

                    }

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
                    userImage = usrProfileData.getPhoto_url();
                    userNick = usrProfileData.getNick();

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

    public void backgroundThreadInitializeFriend(final Context context, final boolean isFriend) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    friendName.setText(userName);
                    if(isFriend){
                        friendStatus.setText("is your friend");
                        addToFriendsButton.setText("DELETE FRIEND");
                        addToFriendsButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                                        getContext());

                                dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteFriend();
                                    }
                                });
                                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                                final AlertDialog alertDialog = dialogBuilder.create();
                                alertDialog.setTitle("Do you want to delete " + userName +" from friends?" );
                                alertDialog.show();
                            }
                        });
                    }
                    else{
                        friendStatus.setText("is not your friend");
                        addToFriendsButton.setText("INVITE TO FRIENDS");
                        addToFriendsButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                                        getContext());

                                dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        inviteToFriends();
                                    }
                                });
                                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                                final AlertDialog alertDialog = dialogBuilder.create();
                                alertDialog.setTitle("Do you want to delete " + userName +" from friends?" );
                                alertDialog.show();
                            }
                        });
                    }
                }
            });
        }
    }

}
