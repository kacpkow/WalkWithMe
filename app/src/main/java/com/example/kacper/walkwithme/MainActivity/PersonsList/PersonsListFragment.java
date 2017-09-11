package com.example.kacper.walkwithme.MainActivity.PersonsList;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kacper.walkwithme.MainActivity.SimpleDividerItemDecoration;
import com.example.kacper.walkwithme.Model.Person;
import com.example.kacper.walkwithme.Model.PhotoData;
import com.example.kacper.walkwithme.Model.PhotoDataDeserializer;
import com.example.kacper.walkwithme.Model.SearchCriteria;
import com.example.kacper.walkwithme.Model.UserProfileData;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonsListFragment extends Fragment {
    private List<Person> persons;
    private RecyclerView rv;
    private int userId = 0;
    String jsonResponse;
    private EditText ageFrom;
    private EditText ageTo;
    private EditText distance;
    private Button searchButton;
    ProgressDialog progressDialog;

    private CheckBox friendsCheckbox;

    private int castAgeFromString = 0;
    private int castAgeToString = 0;
    private Double castDistanceString = 0.0;

    OkHttpClient client;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        client = RequestController.getInstance().getClient();
        userId = getArguments().getInt("USER_ID", 0);
        View rootView = inflater.inflate(R.layout.fragment_persons_list, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rvPersons);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getResources()));

        ageFrom = (EditText) rootView.findViewById(R.id.AgeFrom);
        ageTo = (EditText) rootView.findViewById(R.id.AgeTo);
        distance = (EditText) rootView.findViewById(R.id.Distance);
        searchButton = (Button) rootView.findViewById(R.id.searchWithCriteriaButton);
        friendsCheckbox = (CheckBox) rootView.findViewById(R.id.friends_checkbox);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Searching, please wait ...");
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = Color.argb(255, 0 , 144, 0 );
                int colorTo = Color.BLUE;
                int duration = 250;
                ObjectAnimator.ofObject(searchButton, "backgroundColor", new ArgbEvaluator(), color, colorTo)
                        .setDuration(duration)
                        .start();
                ObjectAnimator.ofObject(searchButton, "backgroundColor", new ArgbEvaluator(), colorTo, color)
                        .setDuration(duration)
                        .start();

                progressDialog.show();
                persons.clear();
                if(ageFrom.getText().toString().length() != 0){
                    castAgeFromString = Integer.parseInt(ageFrom.getText().toString());
                }
                else{
                    castAgeFromString = 0;
                }
                if(ageTo.getText().toString().length() != 0){
                    castAgeToString = Integer.parseInt(ageTo.getText().toString());
                }
                else{
                    castAgeToString = 0;
                }
                if(distance.getText().toString().length() != 0){
                    castDistanceString = Double.parseDouble(distance.getText().toString());
                }
                else{
                    castDistanceString = null;
                }

                initializeData(userId, castAgeFromString, castAgeToString,
                        castDistanceString);
                initializeAdapter();

                progressDialog.dismiss();
            }
        });


        persons = new ArrayList<>();

        friendsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    searchButton.setBackgroundResource(R.drawable.button_search_inactive);
                    searchButton.setEnabled(false);
                    showFriends();
                }
                else{
                    searchButton.setBackgroundResource(R.drawable.button_register_rounded);
                    searchButton.setEnabled(true);
                    initializeData(userId, 0, 100, 100.0);
                }
            }
        });

        progressDialog.show();
        initializeData(userId, 0, 100, 100.0);
        initializeAdapter();
        progressDialog.dismiss();

        return rootView;
    }

    public void showFriends(){

        persons.clear();

        String url = getString(R.string.service_address)+"friends";

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
                Log.e("error", "error while connecting with server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsonResponse = response.body().string();

                Gson objGson = new GsonBuilder().setPrettyPrinting().create();
                Type listType = new TypeToken<List<UserProfileData>>() {
                }.getType();

                try {
                    List<UserProfileData> readFromJson = objGson.fromJson(jsonResponse, listType);
                    DistanceCalculator calculator = new DistanceCalculator();
                    Double distance;
                    Double userLatitude;
                    Double userLongtitude;
                    SharedPreferences settings = getContext().getSharedPreferences("userLocation", Context.MODE_PRIVATE);
                    userLatitude = (double)settings.getFloat("latitude", 0.0f);
                    userLongtitude = (double)settings.getFloat("longtitude", 0.0f);

                    for (UserProfileData userProfileData:readFromJson
                            ) {
                        try {
                            Person person = new Person();

                            person.setCity(userProfileData.getCity());
                            person.setDescription(userProfileData.getDescription());
                            person.setPhoto_url(userProfileData.getPhoto_url());
                            person.setUser_id(userProfileData.getUser_id());
                            person.setFirstName(userProfileData.getFirstName());
                            person.setLastName(userProfileData.getLastName());
                            person.setCity(userProfileData.getCity());
                            person.setBirth_date(userProfileData.getBirth_date());
                            person.setLatitude(userProfileData.getLatitude());
                            person.setLongtitude(userProfileData.getLongtitude());
                            person.setNick(userProfileData.getNick());

                            distance = calculator.distance(person.getLongtitude(), person.getLongtitude(), userLongtitude, userLatitude, "K");
                            person.setDistance(distance.intValue());
                            String year = person.getBirth_date().substring(0, Math.min(person.getBirth_date().length(), 4));
                            String month = person.getBirth_date().substring(5, Math.min(person.getBirth_date().length(), 7));
                            String day = person.getBirth_date().substring(9, Math.min(person.getBirth_date().length(), 11));

                            Calendar c = Calendar.getInstance();
                            int actualYear = c.get(Calendar.YEAR);
                            int actualMonth = c.get(Calendar.MONTH);
                            int actualDay = c.get(Calendar.DAY_OF_MONTH);

                            Double calcAge =Double.valueOf(year) + Double.valueOf(month)/100.0 + Double.valueOf(day)/10000.0;
                            Double actCalcAge = actualYear + actualMonth/100.0 + actualDay/10000.0;
                            Double calc = Math.floor(actCalcAge - calcAge);

                            person.setAge(calc.intValue());

                            persons.add(person);

                        } catch (JsonSyntaxException e) {
                            Log.e("error", "error in syntax in returning json");
                        }
                    }
                }catch (JsonSyntaxException e){
                    Log.e("Json Syntax exception", e.getLocalizedMessage());
                }

                backgroundThreadInitializeAdapter(getActivity().getApplicationContext());
            }
        });

    }

    private void initializeData(int userId, int ageFrom, int ageTo, Double distance) {
        persons.clear();
        SharedPreferences settings = this.getActivity().getSharedPreferences("userLocation", Context.MODE_PRIVATE);
        Float latitude = 0.0f;
        Float longtitude = 0.0f;

        latitude = settings.getFloat("latitude", 0.0f);
        longtitude = settings.getFloat("longtitude", 0.0f);

        String url = getString(R.string.service_address)+"search";

        Gson gson = new Gson();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(PhotoData.class, new PhotoDataDeserializer());
        final Gson retGson = gsonBuilder.setPrettyPrinting().create();

        MediaType mediaType = MediaType.parse("application/json");
        SearchCriteria requestContent = new SearchCriteria(userId, ageFrom, ageTo, distance, (double) latitude, (double) longtitude);
        String prettyJson = gson.toJson(requestContent);
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(requestContent));

        Log.e("jsonbody", gson.toJson(requestContent));

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
                jsonResponse = response.body().string();

                Type listType = new TypeToken<ArrayList<UserProfileData>>() {
                }.getType();

                try {
                    List<UserProfileData> readFromJson = retGson.fromJson(jsonResponse, listType);
                    DistanceCalculator calculator = new DistanceCalculator();
                    Double distance;
                    Double userLatitude;
                    Double userLongtitude;
                    SharedPreferences settings = getContext().getSharedPreferences("userLocation", Context.MODE_PRIVATE);
                    userLatitude = (double)settings.getFloat("latitude", 0.0f);
                    userLongtitude = (double)settings.getFloat("longtitude", 0.0f);

                    for (UserProfileData userProfileData:readFromJson
                            ) {
                        try {
                            Person person = new Person();

                            person.setCity(userProfileData.getCity());
                            person.setDescription(userProfileData.getDescription());
                            person.setPhoto_url(userProfileData.getPhoto_url());
                            person.setUser_id(userProfileData.getUser_id());
                            person.setFirstName(userProfileData.getFirstName());
                            person.setLastName(userProfileData.getLastName());
                            person.setCity(userProfileData.getCity());
                            person.setBirth_date(userProfileData.getBirth_date());
                            person.setLatitude(userProfileData.getLatitude());
                            person.setLongtitude(userProfileData.getLongtitude());
                            person.setNick(userProfileData.getNick());

                            distance = calculator.distance(person.getLongtitude(), person.getLongtitude(), userLongtitude, userLatitude, "K");
                            person.setDistance(distance.intValue());
                            String year = person.getBirth_date().substring(0, Math.min(person.getBirth_date().length(), 4));
                            String month = person.getBirth_date().substring(5, Math.min(person.getBirth_date().length(), 7));
                            String day = person.getBirth_date().substring(8, Math.min(person.getBirth_date().length(), 10));

                            Calendar c = Calendar.getInstance();
                            int actualYear = c.get(Calendar.YEAR);
                            int actualMonth = c.get(Calendar.MONTH);
                            int actualDay = c.get(Calendar.DAY_OF_MONTH);

                            Log.e("age", year+ " " + month + " "+ day);

                            Double calcAge =Double.valueOf(year) + Double.valueOf(month)/100.0 + Double.valueOf(day)/10000.0;
                            Double actCalcAge = actualYear + actualMonth/100.0 + actualDay/10000.0;
                            Double calc = Math.floor(actCalcAge - calcAge);

                            person.setAge(calc.intValue());

                            persons.add(person);

                        } catch (Exception e) {
                            Log.e("error", "error in syntax in returning json");
                        }
                    }
                }catch (JsonSyntaxException e){
                    Log.e("Json Syntax exception", e.getLocalizedMessage());
                }

                backgroundThreadInitializeAdapter(getActivity().getApplicationContext());
            }
        });

    }

    private void initializeAdapter() {
        PersonAdapter adapter = new PersonAdapter(persons, this.getContext());
        rv.setAdapter(adapter);
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

    public void backgroundThreadInitializeAdapter(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    initializeAdapter();
                }
            });


        }
    }
}
