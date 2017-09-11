package com.example.kacper.walkwithme.MainActivity.Conversation;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kacper.walkwithme.Services.MessageChecker;
import com.example.kacper.walkwithme.Services.MessageCheckerCallbacks;
import com.example.kacper.walkwithme.Model.UserMessageData;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment implements MessageCheckerCallbacks {

    private RecyclerView rv;
    private List<UserMessageData> userMessageDataList;
    EditText newMessageText;
    ImageButton sendButton;
    OkHttpClient client;
    Integer userId;
    Integer myId;
    String myPhoto;
    String foreignPhoto;
    Date currentTime;

    private MessageChecker messageChecker;
    private boolean bound = false;

    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_conversation, container, false);
        client = RequestController.getInstance().getClient();
        rv=(RecyclerView)v.findViewById(R.id.rvConversation);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(false);
        ((SimpleItemAnimator) rv.getItemAnimator()).setSupportsChangeAnimations(false);
        newMessageText = (EditText)v.findViewById(R.id.edit_text_conversation);
        sendButton = (ImageButton)v.findViewById(R.id.sendIcon);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newMessageText.getText().length()!= 0){
                    sendMessage(newMessageText.getText().toString());
                    newMessageText.setText("");
                    InputMethodManager inputManager =
                            (InputMethodManager) getActivity().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        userId = getArguments().getInt("userId", 0);

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("USER_ID", Context.MODE_PRIVATE);
        myId = settings.getInt("userId", 0);

        userMessageDataList = new ArrayList<UserMessageData>();
        initializeData(0);
        initializePhotos();

        Intent intent = new Intent(getActivity().getApplicationContext(), MessageChecker.class);
        getActivity().getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        return v;
    }

    public void sendMessage(String message){
        sendMessageToService(message);
    }

    public void sendMessageToService(String message){
        String url = getString(R.string.service_address)+"message/add";
        UserMessageData umd = new UserMessageData();
        umd.setMessage(message);
        currentTime = Calendar.getInstance().getTime();
        android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        umd.setMsgTime(dateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
        Log.e("msg time", umd.getMsgTime());
        umd.setReceiverId(userId);
        umd.setSenderId(myId);
        umd.setStatus("nread");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(umd));

        final Request request;
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("content-type", "application/json")
                .build();

        client = RequestController.getInstance().getClient();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                backgroundThreadShortToast(getActivity().getApplicationContext(), "Sending error. Please try Internet connection");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                Log.e("resp code", String.valueOf(response.code()));

                if(response.code() == 200){
                    backgroundThreadInitializeData(getActivity().getApplicationContext());
                }
                else{

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

    public void initializeData(final Integer option){
        String url = getString(R.string.service_address)+"message/"+String.valueOf(userId);
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
                Log.e("conversationResp", jsonResponse);
                Log.e("conversationCode", String.valueOf(response.code()));
                if (jsonResponse != null) {
                    Gson objGson = new GsonBuilder().setPrettyPrinting().create();
                    Type listType = new TypeToken<List<UserMessageData>>() {
                    }.getType();

                    try {
                        List<UserMessageData> readFromJson = objGson.fromJson(jsonResponse, listType);
                        if (readFromJson != null) {
                            UserMessageData[] messageArray = gson.fromJson(jsonResponse, UserMessageData[].class);
                            userMessageDataList = Arrays.asList(messageArray);
                            if(option == 0){
                                backgroundThreadInitializeAdapter(getActivity().getApplicationContext());
                            }
                            else{
                                backgroundThreadNotificateDataSetChanged(getActivity().getApplicationContext());
                            }
                        }

                    } catch (JsonSyntaxException e) {
                        Log.e("error", "error in syntax in returning json");
                        //backgroundThreadInitializeAdapter(getActivity().getApplicationContext());
                    }

                }
            }
        });
    }

    void initializeAdapter(){
        final ConversationAdapter adapter = new ConversationAdapter(userMessageDataList, myPhoto, foreignPhoto, this.getContext());
        Log.e("initializing", "adapter");
        final RecyclerView.SmoothScroller smoothScroller = new
                LinearSmoothScroller(getActivity().getApplicationContext()) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };
        rv.setAdapter(adapter);
        smoothScroller.setTargetPosition(rv.getAdapter().getItemCount());
        rv.post(new Runnable() {
            @Override
            public void run() {
                rv.getLayoutManager().startSmoothScroll(smoothScroller);
            }
        });

    }

    void initializeAdapterChanged(){
        final ConversationAdapter adapter = new ConversationAdapter(userMessageDataList, myPhoto, foreignPhoto, this.getContext());
        final RecyclerView.SmoothScroller smoothScroller = new
                LinearSmoothScroller(getActivity().getApplicationContext()) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };
        rv.swapAdapter(adapter, false);
        smoothScroller.setTargetPosition(rv.getAdapter().getItemCount());
        rv.post(new Runnable() {
            @Override
            public void run() {
                rv.getLayoutManager().startSmoothScroll(smoothScroller);
            }
        });
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

    public void backgroundThreadInitializePhotos(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    initializePhotos();
                }
            });
        }
    }

    public void backgroundThreadInitializeData(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    initializeData(1);
                }
            });
        }
    }

    public void backgroundThreadNotificateDataSetChanged(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    initializeAdapterChanged();
                }
            });
        }
    }

    public void initializePhotos(){
        initializeMyPhoto();
    }

    public void initializeMyPhoto(){
        String url = getString(R.string.service_address) + "user/"+String.valueOf(myId);

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
                //progressDialog.dismiss();
                String json = response.body().string();
                Gson retGson = new Gson();
                Log.e("myPhoto", json);

                if(response.code() == 200){
                    try{
                        UserProfileData user = retGson.fromJson(json, UserProfileData.class);
                        myPhoto = user.getPhoto_url();
                        initializeForeignPhoto(getActivity().getApplicationContext());
                    }catch(Exception ex){
                        Log.e("ex", ex.getLocalizedMessage());
                    }

                }
            }
        });

    }

    public void initializeForeignPhoto(Context context){
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    initializeForeignPhotoData();
                }
            });
        }
    }

    public void initializeForeignPhotoData(){
        String url = getString(R.string.service_address) + "user/"+String.valueOf(userId);

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
                String json = response.body().string();
                Gson retGson = new Gson();
                Log.e("foreignPhoto", json);

                if(response.code() == 200){
                    try{
                        UserProfileData user = retGson.fromJson(json, UserProfileData.class);
                        foreignPhoto = user.getPhoto_url();
                        backgroundThreadInitializeAdapter(getActivity().getApplicationContext());
                    }catch(Exception ex){
                        Log.e("ex", ex.getLocalizedMessage());
                    }

                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from service
        if (bound) {
            messageChecker.setCallbacks(null); // unregister
            getActivity().getApplicationContext().unbindService(serviceConnection);
            bound = false;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            MessageChecker.LocalBinder binder = (MessageChecker.LocalBinder) service;
            messageChecker = binder.getService();
            bound = true;
            messageChecker.setCallbacks(ConversationFragment.this); // register
            timerHandler.postDelayed(timerRunnable, 0);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };


    long startTime = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            if(bound && rv.getAdapter() != null){
                messageChecker.checkMessages(userId,rv.getAdapter().getItemCount());
            }

            timerHandler.postDelayed(this, 3000);
        }
    };


    @Override
    public void updateMessages() {
        Log.e("updating", "updating");
        initializeData(1);
    }
}
