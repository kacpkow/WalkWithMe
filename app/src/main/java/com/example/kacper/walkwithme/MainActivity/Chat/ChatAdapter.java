package com.example.kacper.walkwithme.MainActivity.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kacper.walkwithme.MainActivity.Conversation.ConversationFragment;
import com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments.ForcomingAppointmentsAdapter;
import com.example.kacper.walkwithme.Model.ChatData;
import com.example.kacper.walkwithme.Model.JsonHelper;
import com.example.kacper.walkwithme.Model.MessageNotifications;
import com.example.kacper.walkwithme.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kacper on 2017-07-24.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        de.hdodenhof.circleimageview.CircleImageView chatPhoto;
        TextView chatPersonName;
        TextView notificationStatus;
        Integer userId;

        public ChatViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_chat);
            chatPhoto = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.chat_photo);
            chatPersonName = (TextView) itemView.findViewById(R.id.chat_label);
            notificationStatus = (TextView) itemView.findViewById(R.id.notificationStatus);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle args = new Bundle();
            args.putInt("userId", userId);
            ConversationFragment newFragment = new ConversationFragment();
            newFragment.setArguments(args);
            FragmentManager fm = ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment f = ((AppCompatActivity)v.getContext()).getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            ft.replace(R.id.fragment_container, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    Context mContext;
    List<ChatData> chatData;
    Map<Integer, MessageNotifications> map;
    String jsonMap;


    ChatAdapter(List<ChatData> chatData, String jsonMap, Context mContext){
        this.chatData = chatData;
        this.mContext = mContext;
        this.jsonMap = jsonMap;
        this.map = JsonHelper.jsonToMapIntegerObject(jsonMap);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item, viewGroup, false);
        ChatViewHolder cvh = new ChatViewHolder(v);
        return cvh;
    }


    @Override
    public void onBindViewHolder(ChatViewHolder chatViewHolder, int i) {
        try {
            Glide.with(mContext)
                    .load(Base64.decode(chatData.get(i).getPersonPhoto(), Base64.DEFAULT))
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate())
                    .into(chatViewHolder.chatPhoto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        chatViewHolder.chatPersonName.setText(chatData.get(i).getFirstName() + " " + chatData.get(i).getLastName());
        chatViewHolder.userId = chatData.get(i).getUserId();

        if(map.get(chatViewHolder.userId) != null) {
            if(map.get(chatData.get(i).getUserId()).getCount()== 1){
                chatViewHolder.notificationStatus.setText("1 message not read");
            }
            else{
                chatViewHolder.notificationStatus.setText(String.valueOf(map.get(chatViewHolder.userId).getCount()) + " messages not read" );
            }

        }
        else
            chatViewHolder.notificationStatus.setText("");

    }

    @Override
    public int getItemCount() {

        return chatData.size();
    }
}
