package com.example.kacper.walkwithme.MainActivity.Conversation;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kacper.walkwithme.Model.UserMessageData;
import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>  {

    public class ConversationViewHolder extends RecyclerView.ViewHolder {

        CardView cv1;
        TextView message;
        de.hdodenhof.circleimageview.CircleImageView personPhoto;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            cv1 = (CardView)itemView.findViewById(R.id.cv_my_message);
            message = (TextView)itemView.findViewById(R.id.my_message);
            personPhoto = (de.hdodenhof.circleimageview.CircleImageView)itemView.findViewById(R.id.userPhoto);
        }

    }

    Context mContext;
    List<UserMessageData> userMessageData;
    Integer myId = 0;
    String myPhoto;
    String foreignPhoto;

    ConversationAdapter(List<UserMessageData> userMessageData, String myPhoto, String foreignPhoto, Context mContext){
        this.userMessageData = userMessageData;
        this.mContext = mContext;
        SharedPreferences settings = mContext.getApplicationContext().getSharedPreferences("USER_ID", Context.MODE_PRIVATE);
        myId = settings.getInt("userId", 0);
        this.myPhoto = myPhoto;
        this.foreignPhoto = foreignPhoto;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ConversationAdapter.ConversationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_message_item, viewGroup, false);
        ConversationViewHolder cvh = new ConversationViewHolder(v);
        return cvh;
    }


    @Override
    public void onBindViewHolder(ConversationViewHolder chatViewHolder, int i) {
        chatViewHolder.message.setText(userMessageData.get(i).getMessage());
        if(userMessageData.get(i).getSenderId() == myId){
            if(myPhoto != null){
                Glide.with(mContext)
                        .load(Base64.decode(myPhoto, Base64.DEFAULT))
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .dontAnimate())
                        .into(chatViewHolder.personPhoto);
            }
            chatViewHolder.message.setBackgroundResource(R.drawable.button_my_message);
            RelativeLayout.LayoutParams messageParams = (RelativeLayout.LayoutParams)chatViewHolder.message.getLayoutParams();
            RelativeLayout.LayoutParams photoParams = (RelativeLayout.LayoutParams)chatViewHolder.personPhoto.getLayoutParams();
            photoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            photoParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            messageParams.setMargins(300,0,10,0);
            messageParams.addRule(RelativeLayout.LEFT_OF,R.id.userPhoto);
            messageParams.removeRule(RelativeLayout.RIGHT_OF);
            chatViewHolder.message.setLayoutParams(messageParams);
            chatViewHolder.personPhoto.setLayoutParams(photoParams);
        }
        else{
            if(foreignPhoto!=null){
                Glide.with(mContext)
                        .load(Base64.decode(foreignPhoto, Base64.DEFAULT))
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .dontAnimate())
                        .into(chatViewHolder.personPhoto);
            }

            chatViewHolder.message.setBackgroundResource(R.drawable.button_foreign_message);
            RelativeLayout.LayoutParams messageParams = (RelativeLayout.LayoutParams)chatViewHolder.message.getLayoutParams();
            RelativeLayout.LayoutParams photoParams = (RelativeLayout.LayoutParams)chatViewHolder.personPhoto.getLayoutParams();
            photoParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            photoParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            messageParams.setMargins(10,0,300,0);

            messageParams.addRule(RelativeLayout.RIGHT_OF,R.id.userPhoto);
            messageParams.removeRule(RelativeLayout.LEFT_OF);
            chatViewHolder.message.setLayoutParams(messageParams);
            chatViewHolder.personPhoto.setLayoutParams(photoParams);
        }
    }

    @Override
    public int getItemCount() {
        return userMessageData.size();
    }
}

