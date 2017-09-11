package com.example.kacper.walkwithme.AppointmentDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kacper.walkwithme.Model.UserProfileData;
import com.example.kacper.walkwithme.PersonDetails.PersonDetailsFragment;
import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * Created by kacper on 2017-09-09.
 */

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantsViewHolder> {
    private Context mContext;

    public class ParticipantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView participantName;
        de.hdodenhof.circleimageview.CircleImageView participantPhoto;
        UserProfileData user;

        ParticipantsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.participantsView);
            participantName = (TextView) cv.findViewById(R.id.participantsName);
            participantPhoto = (de.hdodenhof.circleimageview.CircleImageView) cv.findViewById(R.id.participant_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle args = new Bundle();
            args.putInt("USER_ID", user.getUser_id());
            PersonDetailsFragment newFragment = new PersonDetailsFragment();
            newFragment.setArguments(args);
            FragmentManager fm = ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment f = ((AppCompatActivity)v.getContext()).getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            ft.replace(R.id.fragment_container, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    List<UserProfileData> participantsData;

    ParticipantsAdapter(List<UserProfileData> participantsData, Context mContext) {
        this.participantsData = participantsData;
        this.mContext = mContext;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ParticipantsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.participants_item, viewGroup, false);
        ParticipantsViewHolder pvh = new ParticipantsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ParticipantsViewHolder participantsViewHolder, int i) {
        Log.e("Set name", participantsData.get(i).getFirstName());
        participantsViewHolder.participantName.setText(participantsData.get(i).getFirstName() + " " + participantsData.get(i).getLastName());

        try {
            Glide.with(mContext)
                    .load(Base64.decode(participantsData.get(i).getPhoto_url(), Base64.DEFAULT))
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate())
                    .into(participantsViewHolder.participantPhoto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        participantsViewHolder.user = participantsData.get(i);
    }

    @Override
    public int getItemCount() {
        return participantsData.size();
    }
}
