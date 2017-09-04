package com.example.kacper.walkwithme.MainActivity.Notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kacper.walkwithme.Model.NotificationData;
import com.example.kacper.walkwithme.PersonDetails.PersonDetailsFragment;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by kacper on 2017-06-09.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.StrollRequestViewHolder> {
    private Context mContext;
    private OkHttpClient client = RequestController.getInstance().getClient();

    public class StrollRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView personName;
        TextView invitationType;
        ImageView personPhoto;
        ImageButton acceptButton;
        ImageButton declineButton;

        Integer notificationId;

        String requestNr;
        Integer userId;
        String firstName;
        String lastName;
        String location;
        String startTime;
        String endTime;
        String mediumPhoto;

        private Context context;

        StrollRequestViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvNotification);
            invitationType = (TextView)itemView.findViewById(R.id.notificationTypeField);
            personName = (TextView)itemView.findViewById(R.id.notificationSenderName);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            acceptButton = (ImageButton)itemView.findViewById(R.id.buttonAccept);
            declineButton = (ImageButton)itemView.findViewById(R.id.buttonDecline);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            checkNotification(notificationId);
        }
    }

    public List<NotificationData> getNotificationDataList() {
        return notificationDataList;
    }

    public void setNotificationDataList(List<NotificationData> notificationDataList) {
        this.notificationDataList = notificationDataList;
    }

    List<NotificationData> notificationDataList;
    NotificationsPresenter presenter;

    NotificationsAdapter(List<NotificationData> notificationData, Context mContext, NotificationsPresenter presenter){
        this.notificationDataList = notificationData;
        this.mContext = mContext;
        this.presenter = presenter;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public StrollRequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item, viewGroup, false);
        StrollRequestViewHolder srvh = new StrollRequestViewHolder(v);
        return srvh;
    }

    @Override
    public void onBindViewHolder(final StrollRequestViewHolder strollRequestViewHolder, final int i) {
        strollRequestViewHolder.personName.setText(notificationDataList.get(i).getSender().getFirstName() + " " + notificationDataList.get(i).getSender().getLastName());
        Glide.with(mContext).load(notificationDataList.get(i).getSender().getPhoto_url())
                .into(strollRequestViewHolder.personPhoto);

        Log.e("initialized", "1");

        if(notificationDataList.get(i).getType().equals("Stroll")){
            strollRequestViewHolder.invitationType.setText("stroll");
        }
        else{
            strollRequestViewHolder.invitationType.setText("friends");
        }

        strollRequestViewHolder.notificationId = notificationDataList.get(i).getNotification_id();

        strollRequestViewHolder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strollRequestViewHolder.declineButton.setVisibility(View.GONE);
                strollRequestViewHolder.acceptButton.setVisibility(View.GONE);
                checkNotification(strollRequestViewHolder.notificationId);
                NotificationData notificationData = notificationDataList.get(i);
                notificationDataList.remove(i);
                notificationDataList.add(notificationDataList.size(), notificationData);

                presenter.refreshAdapterElements();
            }
        });

        if(notificationDataList.get(i).getStatus().equals("checked")){
            strollRequestViewHolder.acceptButton.setVisibility(View.GONE);
            strollRequestViewHolder.declineButton.setVisibility(View.GONE);
        }

        strollRequestViewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notificationDataList.get(i).getType().equals("Friend")){
                    strollRequestViewHolder.acceptButton.setVisibility(View.GONE);
                    strollRequestViewHolder.declineButton.setVisibility(View.GONE);
                    checkNotification(strollRequestViewHolder.notificationId);
                    acceptFriend(strollRequestViewHolder.getAdapterPosition());
                }

                presenter.refreshAdapterElements();
            }
        });

        if(notificationDataList.get(i).getType().equals("Stroll")){
            strollRequestViewHolder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notificationDataList.get(i).getStatus().equals("notChecked")){
                        checkNotification(strollRequestViewHolder.notificationId);
                    }
                    FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    StrollNotificationFragment newFragment = new StrollNotificationFragment();
                    Fragment f = ((AppCompatActivity)mContext).getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                    Bundle args = new Bundle();
                    args.putInt("senderId", notificationDataList.get(i).getSender().getUser_id());
                    args.putInt("strollId", notificationDataList.get(i).getEventId());
                    newFragment.setArguments(args);

                    ft.replace(R.id.fragment_container, newFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }
        else{
            strollRequestViewHolder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    PersonDetailsFragment newFragment = new PersonDetailsFragment();
                    Fragment f = ((AppCompatActivity)mContext).getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                    Bundle args = new Bundle();
                    args.putInt("USER_ID_1", notificationDataList.get(i).getSender().getUser_id());

                    newFragment.setArguments(args);

                    ft.replace(R.id.fragment_container, newFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }
    }



    public void checkNotification(Integer requestId){

        String url ="http://10.0.2.2:8080/notification/" + toString().valueOf(requestId);

        Gson gson = new Gson();

        MediaType mediaType = MediaType.parse("application/json");
        String str = null;
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(str));

        final Request request;
        request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
            }
        });

    }

    public void acceptFriend(Integer position){

        String url ="http://10.0.2.2:8080/friends/accept/" + toString().valueOf(notificationDataList.get(position).getSender().getUser_id());
        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();

        MediaType mediaType = MediaType.parse("application/json");
        String str = null;
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(str));

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
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationDataList.size();
    }
}