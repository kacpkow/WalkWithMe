package com.example.kacper.walkwithme.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.example.kacper.walkwithme.Model.JsonHelper;
import com.example.kacper.walkwithme.Model.MessageNotifications;
import com.example.kacper.walkwithme.Model.NotificationData;
import com.example.kacper.walkwithme.Model.UserMessageData;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationChecker extends Service {

    private NotificationCheckerCallbacks notificationCheckerCallbacks;

    private final IBinder binder = new LocalBinder();

    private OkHttpClient client;

    int count = 0;

    Map<Integer, MessageNotifications> messageNotificationsList;
    MessageNotifications singleNotification;

    public NotificationChecker() {
    }

    public class LocalBinder extends Binder {
        public NotificationChecker getService() {
            return NotificationChecker.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        messageNotificationsList = new HashMap<Integer, MessageNotifications>();
        client = RequestController.getInstance().getClient();
        return binder;
    }

    public void setCallbacks(NotificationCheckerCallbacks callbacks) {
        notificationCheckerCallbacks = callbacks;
    }

    public void notificate(){
        if(notificationCheckerCallbacks != null){
            notificationCheckerCallbacks.updateNotificationFromNotifications();
        }
    }

    public void notificateMessages(){
        if(notificationCheckerCallbacks != null){
            String json = JsonHelper.mapStringObjectToJson(messageNotificationsList);
            notificationCheckerCallbacks.updateMessageNotificationsFromService(json);
            messageNotificationsList.clear();
        }
    }


    public void checkNotifications(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("userId", Context.MODE_PRIVATE);
        Integer userId = settings.getInt("ID", 0);

        String url = getString(R.string.service_address) + "notification";

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
                String jsonResponse = response.body().string();
                if(jsonResponse != null){
                    Gson objGson = new GsonBuilder().setPrettyPrinting().create();
                    Type listType = new TypeToken<List<NotificationData>>() {
                    }.getType();

                    try{
                        List<NotificationData> readFromJson = objGson.fromJson(jsonResponse, listType);
                        if(readFromJson != null){
                            for (NotificationData notificationData :readFromJson) {
                                if(notificationData.getStatus().equals("notChecked")){
                                    backgroundThreadNotificate(getApplicationContext());
                                }
                            }
                        }

                    }  catch (JsonSyntaxException e) {
                        Log.e("error", "error in syntax in returning json");
                    }

                }
            }
        });
    }

    public void checkMessages(){
        count = 0;

        String url = getString(R.string.service_address) + "message/";

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
                String jsonResponse = response.body().string();
                Log.e("msgs", jsonResponse);
                if(jsonResponse != null){
                    Gson objGson = new GsonBuilder().setPrettyPrinting().create();
                    Type listType = new TypeToken<List<UserMessageData>>() {
                    }.getType();

                    try{
                        List<UserMessageData> readFromJson = objGson.fromJson(jsonResponse, listType);
                        if(readFromJson != null){
                            for (UserMessageData userMessageData :readFromJson) {
                                if(userMessageData.getStatus().equals("nread")){
                                    singleNotification = new MessageNotifications();
                                    singleNotification.setUserId(userMessageData.getSenderId());
                                    if(messageNotificationsList.get(userMessageData.getSenderId()) != null){
                                        singleNotification.setCount(messageNotificationsList.get(singleNotification.getUserId()).getCount() + 1);
                                    }
                                    else{
                                        singleNotification.setCount(1);
                                    }

                                    messageNotificationsList.remove(singleNotification.getUserId());
                                    messageNotificationsList.put(singleNotification.getUserId(), singleNotification);
                                    count++;
                                }
                            }
                            if(count != 0){
                                backgroundThreadNotificateMsg(getApplicationContext());
                            }
                        }

                    }  catch (JsonSyntaxException e) {
                        Log.e("error", "error in syntax in returning json");
                    }

                }
            }
        });
    }

    public void backgroundThreadNotificate(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    notificate();
                }
            });
        }
    }

    public void backgroundThreadNotificateMsg(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    notificateMessages();
                }
            });
        }
    }
}
