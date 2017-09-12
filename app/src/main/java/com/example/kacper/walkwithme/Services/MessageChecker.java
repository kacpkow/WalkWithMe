package com.example.kacper.walkwithme.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.example.kacper.walkwithme.Model.UserMessageData;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
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
public class MessageChecker extends Service {
    private MessageCheckerCallbacks messageCheckerCallbacks;

    private final IBinder binder = new MessageChecker.LocalBinder();

    private OkHttpClient client;
    private String jsonResponse;
    private Gson objGson;
    private Type listType;
    private List<UserMessageData> readFromJson;

    public MessageChecker() {
    }

    public class LocalBinder extends Binder {
        public MessageChecker getService() {
            return MessageChecker.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        client = RequestController.getInstance().getClient();
        objGson = new GsonBuilder().setPrettyPrinting().create();
        listType = new TypeToken<List<UserMessageData>>() {
        }.getType();
        return binder;
    }

    public void setCallbacks(MessageCheckerCallbacks callbacks) {
        messageCheckerCallbacks = callbacks;
    }

    public void updateMessages(){
        if (messageCheckerCallbacks != null){
            messageCheckerCallbacks.updateMessages();
        }

    }


    public void checkMessages(int userId, final int count){

        String url = getString(R.string.service_address) + "message/" + String.valueOf(userId) ;

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
            public void onResponse(Call call, Response response) throws IOException {
                jsonResponse = response.body().string();
                if(jsonResponse != null){

                    try{
                        readFromJson = objGson.fromJson(jsonResponse, listType);
                        if(readFromJson != null && readFromJson.size() != count){
                            Log.e("should", "initialize");
                            backgroundThreadNotificate(getApplicationContext());
                        }

                    }  catch (JsonSyntaxException e) {

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
                    updateMessages();
                }
            });
        }
    }
}
