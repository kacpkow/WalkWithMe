package com.example.kacper.walkwithme.MainActivity.Conversation;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kacper.walkwithme.MainActivity.Chat.ChatAdapter;
import com.example.kacper.walkwithme.Model.UserMessageData;
import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * Created by kacper on 2017-08-31.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>  {

    public class ConversationViewHolder extends RecyclerView.ViewHolder {

        CardView cv1;
        TextView message;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            cv1 = (CardView)itemView.findViewById(R.id.cv_my_message);
            message = (TextView)itemView.findViewById(R.id.my_message);
            message.setBackgroundResource(R.drawable.button_foreign_message);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)message.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            message.setLayoutParams(params);
        }

    }

    Context mContext;
    List<UserMessageData> userMessageData;

    ConversationAdapter(List<UserMessageData> userMessageData, Context mContext){
        this.userMessageData = userMessageData;
        this.mContext = mContext;
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

    }

    @Override
    public int getItemCount() {
//        return userMessageData.size();
        return 1;
    }
}
