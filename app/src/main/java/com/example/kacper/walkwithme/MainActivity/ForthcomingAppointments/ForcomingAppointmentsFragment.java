package com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.kacper.walkwithme.LoginActivity.LoginActivity;
import com.example.kacper.walkwithme.LoginActivity.LoginContent;
import com.example.kacper.walkwithme.MainActivity.SimpleDividerItemDecoration;
import com.example.kacper.walkwithme.Model.StrollData;
import com.example.kacper.walkwithme.Model.User;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */
public class ForcomingAppointmentsFragment extends Fragment {
    private List<StrollData> strollDataList;
    private RecyclerView rv;
    private Integer userId;
    ProgressDialog dialog;

    OkHttpClient client;


    public void tryToLogIn(){

        String url = getString(R.string.service_address) + "rest/login";
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE);
        String json = settings.getString("values", "");
        Gson gson1 = new Gson();
        LoginContent log = gson1.fromJson(json, LoginContent.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(log));

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
                //goBackToLoginPage(getActivity().getApplicationContext());
                backgroundThreadShortToast(getActivity().getApplicationContext(), "Connection error. Please check your Internet connection status");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Gson retGson = new Gson();

                if(response.code() == 200){
                    getUser();
                }
                else{
                    //TO DO log out
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

    public void getUser(){

        String url = getString(R.string.service_address) + "user";

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

                    User usr = retGson.fromJson(json, User.class);

                    RequestController.getInstance().setState(true);

                    userId = usr.getUser_id();

                    backgroundThreadInitializeData(getActivity().getApplicationContext());

                }
                else{
                    Log.e("get data", json);
                }

            }
        });

    }


    public void goBackToLoginPage(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }


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
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = settings.getInt("userId", 0);
        Log.e("1initialize fr", "1");

        if(userId == 0){
            tryToLogIn();
            Log.e("1", "1");

        }
        else{
            initializeData();
            initializeAdapter();
            Log.e("2", "2");
        }

        return rootView;
    }

    private void initializeData(){
        if(getActivity().getApplicationContext() != null){
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Loading, please wait");
            dialog.show();

        }

        String url = getString(R.string.service_address)+"stroll/get";
        final Gson gson = new Gson();

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
                backgroundThreadInitializeAdapter(getActivity().getApplicationContext());
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
                            StrollData[] strollArray = gson.fromJson(jsonResponse, StrollData[].class);
                            strollDataList = Arrays.asList(strollArray);
                        }

                    } catch (JsonSyntaxException e) {
                        Log.e("error", "error in syntax in returning json");
                    }
                    backgroundThreadInitializeAdapter(getActivity().getApplicationContext());

                }
            }
        });

    }

    @Override
    public void onResume(){
        //initializeData();
        if(dialog != null){

        }
        super.onResume();
    }

    private void initializeAdapter(){
        ForcomingAppointmentsAdapter adapter = new ForcomingAppointmentsAdapter(strollDataList, this.getContext());
        dialog.dismiss();
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

    public void backgroundThreadInitializeData(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    initializeData();
                }
            });
        }
    }

    @Override
    public void onPause(){
        if(dialog != null){
            dialog.dismiss();
        }

        super.onPause();
    }

    @Override
    public void onDestroy(){
        if(dialog != null){
            dialog.dismiss();
        }
        super.onDestroy();
    }

}