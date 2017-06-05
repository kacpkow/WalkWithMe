package com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments;

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

import com.example.kacper.walkwithme.MainActivity.PersonsList.Person;
import com.example.kacper.walkwithme.MainActivity.PersonsList.SearchContent;
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

public class ForcomingAppointmentsFragment extends Fragment {
    private List<ForcomingAppointment> forcomingAppointments;
    private RecyclerView rv;
    private Integer userId;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointments, container, false);

        rv=(RecyclerView)rootView.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        userId = 0;
        forcomingAppointments = new ArrayList<>();
        initializeData();
        initializeAdapter();

        return rootView;
    }

    private void initializeData(){
        SharedPreferences settings = this.getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = settings.getInt("ID", 0);
/*
        String url ="http://10.0.2.2:8080/GetAppointmentsAndroid";
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        MediaType mediaType = MediaType.parse("application/json");
        AppointmentsContent requestContent = new AppointmentsContent(userId);
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
                Log.e("error", "error while connectinh with server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson retGson = new Gson();
                String jsonResponse = response.body().String();
                ForcomingAppointment forcomingAppointment;
                for (int i = 0; i < response.body().contentLength(); i++) {
                    try {
                        forcomingAppointment = retGson.fromJson(jsonResponse, ForcomingAppointment.class);
                        forcomingAppointments.add(forcomingAppointment);

                    } catch (JsonSyntaxException e) {
                        Log.e("error", "error in syntax in returning json");
                    }
                }
            }
        });
*/
        forcomingAppointments.add(new ForcomingAppointment(1, userId, "Brad", "Pitt", "Kraków", "23.05.2017", "17:00", "http://www.a-listinternational.com/wp-content/uploads/2016/06/brad-pitt-doesn-t-really-look-much-like-brad-pitt-in-these-photos-727400.jpg"));
    }

    private void initializeAdapter(){
        ForcomingAppointmentsAdapter adapter = new ForcomingAppointmentsAdapter(forcomingAppointments, this.getContext());
        rv.setAdapter(adapter);
    }
}