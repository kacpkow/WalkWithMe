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
import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * Created by kacper on 2017-04-04.
 */

public class ForcomingAppointmentsAdapter extends RecyclerView.Adapter<ForcomingAppointmentsAdapter.AppointmentViewHolder> {
    private Context mContext;

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView personName;
        TextView locationView;
        TextView datetime;
        ImageView personPhoto;
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
            personName = (TextView)itemView.findViewById(R.id.person_name);
            locationView = (TextView)itemView.findViewById(R.id.locationData);
            datetime = (TextView)itemView.findViewById(R.id.datetime);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            detailsButton = (Button)itemView.findViewById(R.id.editButton);
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

    List<ForcomingAppointment> forcomingAppointments;

    ForcomingAppointmentsAdapter(List<ForcomingAppointment> forcomingAppointments, Context mContext){
        this.forcomingAppointments = forcomingAppointments;
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
    public void onBindViewHolder(AppointmentViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(forcomingAppointments.get(i).firstName + " " + forcomingAppointments.get(i).lastName);
        personViewHolder.locationView.setText(forcomingAppointments.get(i).location);
        personViewHolder.datetime.setText(forcomingAppointments.get(i).date + " at " + forcomingAppointments.get(i).time);
        Glide.with(mContext).load(forcomingAppointments.get(i).getMediumPhoto())
                .into(personViewHolder.personPhoto);
        personViewHolder.userId = forcomingAppointments.get(i).userId;
        personViewHolder.firstName = forcomingAppointments.get(i).firstName;
        personViewHolder.lastName = forcomingAppointments.get(i).lastName;
        personViewHolder.location= forcomingAppointments.get(i).location;
        personViewHolder.date = forcomingAppointments.get(i).date;
        personViewHolder.time = forcomingAppointments.get(i).time;
        personViewHolder.mediumPhoto = forcomingAppointments.get(i).mediumPhoto;
        personViewHolder.strollId = forcomingAppointments.get(i).strollId;
    }

    @Override
    public int getItemCount() {
        return forcomingAppointments.size();
    }
}