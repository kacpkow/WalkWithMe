package com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kacper.walkwithme.AppointmentDetails.AppointmentDetailsActivity;
import com.example.kacper.walkwithme.Model.StrollData;
import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * Created by kacper on 2017-04-04.
 */

public class ForcomingAppointmentsAdapter extends RecyclerView.Adapter<ForcomingAppointmentsAdapter.AppointmentViewHolder> {
    private Context mContext;

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView strollStartTime;
        TextView strollEndTime;
        TextView locationView;
        Button detailsButton;

        Integer strollId;
        Integer userId;
        String firstName;
        String lastName;
        String location;
        String date;
        String time;
        String mediumPhoto;

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
            context = v.getContext();
            Intent intent = new Intent(context, AppointmentDetailsActivity.class);
            intent.putExtra("STROLL_ID", strollId);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("LOCATION", location);
            intent.putExtra("DATE", date);
            intent.putExtra("TIME", time);
            intent.putExtra("USER_FIRST_NAME", firstName);
            intent.putExtra("USER_LAST_NAME", lastName);
            intent.putExtra("USER_IMAGE", mediumPhoto);

            context.startActivity(intent);

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
    }

    @Override
    public int getItemCount() {
        return strollDataList.size();
    }
}