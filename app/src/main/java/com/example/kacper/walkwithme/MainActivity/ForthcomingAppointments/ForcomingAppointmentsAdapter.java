package com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments;

import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;

import com.example.kacper.walkwithme.AppointmentDetails.StrollDetailsFragment;
import com.example.kacper.walkwithme.Model.StrollData;
import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * Created by kacper on 2017-04-04.
 */

public class ForcomingAppointmentsAdapter extends RecyclerView.Adapter<ForcomingAppointmentsAdapter.AppointmentViewHolder> {
    private Context mContext;

    public class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView strollStartTime;
        TextView strollEndTime;
        TextView locationView;
        Button detailsButton;

        StrollData strollData;

        private Context context;

        AppointmentViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            locationView = (TextView)itemView.findViewById(R.id.locationData);
            strollStartTime = (TextView)itemView.findViewById(R.id.strollStartTime);
            strollEndTime = (TextView)itemView.findViewById(R.id.strollEndTime);
            detailsButton = (Button)itemView.findViewById(R.id.detailsButton);
            detailsButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            StrollDetailsFragment newFragment = new StrollDetailsFragment();
            Fragment f = ((AppCompatActivity)mContext).getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            Bundle args = new Bundle();
            Log.e("Stroll id in adapter", String.valueOf(strollData.getStrollId()));
            args.putInt("STROLL_ID", strollData.getStrollId());
            args.putString("privacy", strollData.getPrivacy());
            args.putString("startTime", strollData.getData_start());
            args.putString("endTime", strollData.getData_end());
            args.putString("location", strollData.getLocation().getDescription());
            args.putString("description", strollData.getInfo());
            args.putInt("participants", strollData.getUser());
            newFragment.setArguments(args);

            ft.replace(R.id.fragment_container, newFragment);
            ft.addToBackStack(null);
            ft.commit();

        }
    }

    List<StrollData> strollDataList;

    ForcomingAppointmentsAdapter(List<StrollData> strollData, Context mContext){
        this.strollDataList = strollData;
        this.mContext = mContext;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AppointmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.appointments_item, viewGroup, false);
        AppointmentViewHolder avh = new AppointmentViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(AppointmentViewHolder appointmentViewHolder, int i) {
        appointmentViewHolder.strollStartTime.setText(appointmentViewHolder.strollStartTime.getText().toString() + strollDataList.get(i).getData_start());
        appointmentViewHolder.strollEndTime.setText(appointmentViewHolder.strollEndTime.getText().toString() + strollDataList.get(i).getData_end());
        appointmentViewHolder.locationView.setText(appointmentViewHolder.locationView.getText().toString() + strollDataList.get(i).getLocation().getDescription());
        appointmentViewHolder.strollData = strollDataList.get(i);
    }

    @Override
    public int getItemCount() {
        return strollDataList.size();
    }
}