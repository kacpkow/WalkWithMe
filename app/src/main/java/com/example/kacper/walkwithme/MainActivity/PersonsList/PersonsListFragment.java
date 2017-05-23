package com.example.kacper.walkwithme.MainActivity.PersonsList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.kacper.walkwithme.MainActivity.SimpleDividerItemDecoration;
import com.example.kacper.walkwithme.R;
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

public class PersonsListFragment extends Fragment {
    private List<com.example.kacper.walkwithme.MainActivity.PersonsList.Person> persons;
    private RecyclerView rv;
    private int userId;
    String jsonResponse;
    private EditText ageFrom;
    private EditText ageTo;
    private EditText distance;
    private Button searchButton;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userId = getArguments().getInt("USER_ID", 0);
        View rootView = inflater.inflate(R.layout.fragment_persons_list, container, false);

        rv=(RecyclerView)rootView.findViewById(R.id.rvPersons);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getResources()));

        ageFrom = (EditText)rootView.findViewById(R.id.AgeFrom);
        ageTo = (EditText)rootView.findViewById(R.id.AgeTo);
        distance = (EditText)rootView.findViewById(R.id.Distance);
        searchButton = (Button)rootView.findViewById(R.id.searchWithCriteriaButton);
        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setTitle("Searching, please wait ...");
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                persons.clear();
                initializeData(userId, ageFrom.getText().toString(), ageTo.getText().toString(),
                       distance.getText().toString());
                initializeAdapter();
                progressDialog.dismiss();
            }
        });

        persons = new ArrayList<>();

        progressDialog.show();
        initializeData(userId, null, null, null);
        initializeAdapter();
        progressDialog.dismiss();

        return rootView;
    }

    private void initializeData(int userId, String ageFrom, String ageTo, String distance){
        SharedPreferences settings = this.getActivity().getSharedPreferences("userLocation", Context.MODE_PRIVATE);
        Float latitude = 0.0f;
        Float longtitude = 0.0f;

        latitude = settings.getFloat("latitude", 0.0f);
        longtitude = settings.getFloat("longtitude", 0.0f);

        String url ="http://10.0.2.2:8080/GetPersonsAndroid";
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        MediaType mediaType = MediaType.parse("application/json");
        SearchContent requestContent = new SearchContent(userId, ageFrom, ageTo, distance, (double)latitude, (double)longtitude);
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(requestContent));
/*
        Person person = new Person();
        person.setId(101);
        person.setAge(19);
        person.setDistance(12);
        person.setFirstName("Donald");
        person.setLastName("Trump");
        person.setCity("Washington DC");
        person.setLargeImage("http://static4.businessinsider.com/image/56c640526e97c625048b822a-480/donald-trump.jpg");
        person.setPersonDescription("I'm a fckin boss...");
        person.setMediumImage("https://pbs.twimg.com/profile_images/622622832103587840/tUKC5wZD.jpg");
        persons.add(person);

        Person person1 = new Person();
        person1.setId(45);
        person1.setAge(46);
        person1.setDistance(50);
        person1.setFirstName("Angelina");
        person1.setLastName("Jolie");
        person1.setPersonDescription("Brad Pitt is a dumbass for real");
        person1.setCity("NY");
        person1.setLargeImage("https://pbs.twimg.com/profile_images/740895191003975681/kTD5CP9x.jpg");
        person1.setMediumImage("https://pbs.twimg.com/profile_images/740895191003975681/kTD5CP9x.jpg");
        persons.add(person1);


*/


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
                jsonResponse = response.body().toString();
                Person person;
                for (int i = 0; i < response.body().contentLength(); i++) {
                    try {
                        person = retGson.fromJson(jsonResponse, Person.class);
                        persons.add(person);

                    } catch (JsonSyntaxException e) {
                        Log.e("error", "error in syntax in returning json");
                    }
                }
            }
        });
    }

    private void initializeAdapter(){
        PersonAdapter adapter = new PersonAdapter(persons, this.getContext() );
        rv.setAdapter(adapter);
    }

}
