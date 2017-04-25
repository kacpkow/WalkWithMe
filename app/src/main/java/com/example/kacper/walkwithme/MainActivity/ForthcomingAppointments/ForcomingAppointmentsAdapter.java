package com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * Created by kacper on 2017-04-04.
 */

public class ForcomingAppointmentsAdapter extends RecyclerView.Adapter<ForcomingAppointmentsAdapter.AppointmentViewHolder> {

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView personName;
        TextView location;
        TextView datetime;
        ImageView personPhoto;

        AppointmentViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            location = (TextView)itemView.findViewById(R.id.location);
            datetime = (TextView)itemView.findViewById(R.id.datetime);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    List<ForcomingAppointment> forcomingAppointments;

    ForcomingAppointmentsAdapter(List<ForcomingAppointment> forcomingAppointments){
        this.forcomingAppointments = forcomingAppointments;
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
        personViewHolder.personName.setText(forcomingAppointments.get(i).name);
        personViewHolder.location.setText(forcomingAppointments.get(i).location);
        personViewHolder.datetime.setText(forcomingAppointments.get(i).datetime);
        personViewHolder.personPhoto.setImageResource(forcomingAppointments.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return forcomingAppointments.size();
    }
}