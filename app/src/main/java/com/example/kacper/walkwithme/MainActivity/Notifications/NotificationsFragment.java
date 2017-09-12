package com.example.kacper.walkwithme.MainActivity.Notifications;

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
import com.example.kacper.walkwithme.Model.NotificationData;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */
public class NotificationsFragment extends Fragment implements NotificationsView {
    private NotificationsPresenter notificationsPresenter;
    private List<NotificationData> notificationDataList;
    private RecyclerView rv;
    private Integer userId;
    private NotificationsAdapter adapter;
    OkHttpClient client;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        rv=(RecyclerView)rootView.findViewById(R.id.rvStrollRequests);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getResources()));

        notificationDataList = new ArrayList<>();
        notificationsPresenter = new NotificationsPresenterImpl(this);

        client = RequestController.getInstance().getClient();

        Log.e("err", "initializing data");
        initializeData();
        Log.e("err", "initializing adapter");
        initializeAdapter();

        return rootView;
    }

    private void initializeData(){

        SharedPreferences settings = this.getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = settings.getInt("ID", 0);

        String url = getString(R.string.service_address)  + "notification";

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
                Log.e("err", "err");
                String jsonResponse = response.body().string();
                Log.e("err", "err1");
                Log.e("responsecode", toString().valueOf(response.code()));
                if(jsonResponse != null){
                    Gson objGson = new GsonBuilder().setPrettyPrinting().create();
                    Type listType = new TypeToken<List<NotificationData>>() {
                    }.getType();

                    List<NotificationData> notificationCheckedList = new ArrayList<NotificationData>();

                    try{
                        List<NotificationData> readFromJson = objGson.fromJson(jsonResponse, listType);
                        if(readFromJson != null){
                            for (NotificationData notificationData :readFromJson
                                    ) {
                                NotificationData notification = new NotificationData();
                                notification.setEventId(notificationData.getEventId());
                                notification.setNotification_id(notificationData.getNotification_id());
                                notification.setSender(notificationData.getSender());
                                notification.setStatus(notificationData.getStatus());
                                notification.setType(notificationData.getType());

                                if(notification.getStatus().equals("checked")){
                                    notificationCheckedList.add(notification);
                                }
                                else{
                                    notificationDataList.add(notification);
                                }

                            }
                            for(NotificationData notificationData:notificationCheckedList){
                                notificationDataList.add(notificationData);
                            }
                            backgroundThreadInitializeAdapter(getActivity().getApplicationContext());
                        }

                    }  catch (JsonSyntaxException e) {
                        Log.e("error", "error in syntax in returning json");
                    }

                }

            }
        });

    }

    public void backgroundThreadInitializeAdapter(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void initializeAdapter(){
        adapter = new NotificationsAdapter(notificationDataList, this.getContext(), notificationsPresenter);
        rv.setAdapter(adapter);
    }

    @Override
    public void refreshElements() {
        notificationDataList.clear();
        initializeData();
    }
}