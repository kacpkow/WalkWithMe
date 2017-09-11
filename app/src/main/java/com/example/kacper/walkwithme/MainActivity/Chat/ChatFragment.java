package com.example.kacper.walkwithme.MainActivity.Chat;


import android.content.Context;
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
import com.example.kacper.walkwithme.Model.ChatData;
import com.example.kacper.walkwithme.Model.JsonHelper;
import com.example.kacper.walkwithme.Model.MessageNotifications;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private RecyclerView rv;
    private List<ChatData> chatDataList;
    OkHttpClient client;

    Map<Integer, MessageNotifications> map;

    String jsonMap;
    boolean initializeFromMap = false;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        map = new HashMap<Integer, MessageNotifications>();
        client = RequestController.getInstance().getClient();
        rv=(RecyclerView)v.findViewById(R.id.rvChat);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        chatDataList = new ArrayList<>();
        jsonMap = getArguments().getString("messageNotifications", "");
        if(!jsonMap.equals("")){
            map = JsonHelper.jsonToMapIntegerObject(jsonMap);
            initializeFromMap = true;
        }
        initializeData();
        return v;
    }

    public void initializeData(){

        String url = getString(R.string.service_address)+"message/recentChat";
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson retGson = new Gson();
                String jsonResponse = response.body().string();
                Log.e("chatResp", jsonResponse);
                Log.e("chatCode", String.valueOf(response.code()));
                if (jsonResponse != null) {
                    Gson objGson = new GsonBuilder().setPrettyPrinting().create();
                    Type listType = new TypeToken<List<ChatData>>() {
                    }.getType();

                    try {
                        List<ChatData> readFromJson = objGson.fromJson(jsonResponse, listType);
                        if (readFromJson != null) {
                            ChatData[] chatArray = gson.fromJson(jsonResponse, ChatData[].class);
                            chatDataList = Arrays.asList(chatArray);
                            if(getActivity().getApplicationContext() != null){
                                backgroundThreadInitializeAdapter(getActivity().getApplicationContext());
                            }
                        }

                    } catch (JsonSyntaxException e) {
                        Log.e("error", "error in syntax in returning json");
                        backgroundThreadInitializeAdapter(getActivity().getApplicationContext());
                    }

                }
            }
        });

    }

    void initializeAdapter(){
        ChatAdapter adapter = new ChatAdapter(chatDataList, jsonMap, this.getContext());
        rv.setAdapter(adapter);
        Log.e("init", "adapter");
        if(initializeFromMap){
            initializeFromMap = false;
            jsonMap = null;
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

//    @Override
//    public void onResume(){
//        jsonMap = null;
//        super.onResume();
//    }

}
