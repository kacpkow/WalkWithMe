package com.example.kacper.walkwithme.MainActivity.PersonsList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

        initializeData(userId);
        initializeAdapter();

        return rootView;
    }

    private void initializeData(int userId){
        persons = new ArrayList<>();
        String url ="http://10.0.2.2:8080/GetPersonsAndroid";
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        Integer id = userId;
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(userId));

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
        PersonAdapter adapter = new PersonAdapter(persons, this.getContext());
        rv.setAdapter(adapter);
    }

}
