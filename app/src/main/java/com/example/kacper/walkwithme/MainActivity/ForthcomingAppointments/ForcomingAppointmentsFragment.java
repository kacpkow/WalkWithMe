package com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.kacper.walkwithme.MainActivity.SimpleDividerItemDecoration;
import com.example.kacper.walkwithme.Model.StrollData;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForcomingAppointmentsFragment extends Fragment {
    private List<StrollData> strollDataList;
    private RecyclerView rv;
    private Integer userId;

    OkHttpClient client;

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
        strollDataList = new ArrayList<>();
        client = RequestController.getInstance().getClient();
        initializeData();
        initializeAdapter();

        return rootView;
    }

    private void initializeData(){
        SharedPreferences settings = this.getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = settings.getInt("ID", 0);

        String url ="http://10.0.2.2:8080/stroll/get";
        final Gson gson = new Gson();
        MediaType mediaType = MediaType.parse("application/json");
        AppointmentsContent requestContent = new AppointmentsContent(userId);
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(requestContent));
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
                Log.e("jsonResp", jsonResponse);
                Log.e("jsonResp", String.valueOf(response.code()));
                if (jsonResponse != null) {
                    Gson objGson = new GsonBuilder().setPrettyPrinting().create();
                    Type listType = new TypeToken<List<StrollData>>() {
                    }.getType();

                    try {
                        List<StrollData> readFromJson = objGson.fromJson(jsonResponse, listType);
                        if (readFromJson != null) {
//                            for (StrollData strollData : readFromJson
//                                    ) {
//                                Log.e("str idd", strollData.getData_start());
//
//                                StrollData newStrollData = new StrollData();
//                                newStrollData = strollData;
//                                strollDataList.add(newStrollData);
//                            }
                            StrollData[] strollArray = gson.fromJson(jsonResponse, StrollData[].class);
                            strollDataList = Arrays.asList(strollArray);
                            Log.e("str idd", Integer.toString(strollDataList.get(0).getStrollId()));
                            if(getActivity().getApplicationContext() != null){
                                backgroundThreadInitializeAdapter(getActivity().getApplicationContext());
                            }
                        }

                    } catch (JsonSyntaxException e) {
                        Log.e("error", "error in syntax in returning json");
                        backgroundThreadFinishActivity(getActivity().getApplicationContext());
                    }

                }
            }
        });

    }

    @Override
    public void onResume(){
        initializeData();
        super.onResume();
    }

    private void initializeAdapter(){
        ForcomingAppointmentsAdapter adapter = new ForcomingAppointmentsAdapter(strollDataList, this.getContext());
        rv.setAdapter(adapter);
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

    public void backgroundThreadFinishActivity(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    getActivity().onBackPressed();
                }
            });
        }
    }
}