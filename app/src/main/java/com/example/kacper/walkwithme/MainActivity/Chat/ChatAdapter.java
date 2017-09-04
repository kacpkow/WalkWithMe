package com.example.kacper.walkwithme.MainActivity.Chat;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kacper.walkwithme.MainActivity.Conversation.ConversationFragment;
import com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments.ForcomingAppointmentsAdapter;
import com.example.kacper.walkwithme.Model.ChatData;
import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * Created by kacper on 2017-07-24.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        ImageView chatPhoto;
        TextView chatPersonName;

        public ChatViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_chat);
            chatPhoto = (ImageView) itemView.findViewById(R.id.chat_photo);
            chatPersonName = (TextView) itemView.findViewById(R.id.chat_label);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ConversationFragment newFragment = new ConversationFragment();
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

    ChatAdapter(List<ChatData> chatData, Context mContext){
        this.chatData = chatData;
        this.mContext = mContext;
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
        chatViewHolder.chatPhoto.setImageResource(R.drawable.chat_icon);
        //chatViewHolder.chatPhoto.setText(chatViewHolder.strollStartTime.getText().toString() + strollDataList.get(i).getData_start());
        chatViewHolder.chatPersonName.setText(chatData.get(i).getName());
    }

    @Override
    public int getItemCount() {

        return chatData.size();
    }
}
