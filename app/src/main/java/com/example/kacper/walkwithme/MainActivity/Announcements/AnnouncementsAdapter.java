package com.example.kacper.walkwithme.MainActivity.Announcements;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * Created by kacper on 2017-06-13.
 */

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementViewHolder>{
    private Context mContext;

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView location;
        TextView strollStartTime;
        TextView strollEndTime;
        TextView adEndTime;
        Button detailsButton;

        private Context context;

        AnnouncementViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_announcements);
            location = (TextView)itemView.findViewById(R.id.announcement_location_label);
            strollStartTime = (TextView)itemView.findViewById(R.id.announcement_stroll_start_time);
            strollEndTime = (TextView)itemView.findViewById(R.id.announcement_stroll_end_time);
            adEndTime = (TextView)itemView.findViewById(R.id.announcement_ad_end_time);
            detailsButton = (Button)itemView.findViewById(R.id.announcement_edit_Button);
            detailsButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context = v.getContext();
//            Intent intent = new Intent(context, AppointmentDetailsActivity.class);
//            intent.putExtra("STROLL_ID", strollId);
//            intent.putExtra("USER_ID", userId);
//            intent.putExtra("LOCATION", location);
//            intent.putExtra("DATE", date);
//            intent.putExtra("TIME", time);
//            intent.putExtra("USER_FIRST_NAME", firstName);
//            intent.putExtra("USER_LAST_NAME", lastName);
//            intent.putExtra("USER_IMAGE", mediumPhoto);
//
//            context.startActivity(intent);

        }
    }

    List<Announcement> announcements;

    AnnouncementsAdapter(List<Announcement> announcements, Context mContext){
        this.announcements = announcements;
        this.mContext = mContext;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AnnouncementViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.announcements_item, viewGroup, false);
        AnnouncementViewHolder avh = new AnnouncementViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(AnnouncementViewHolder announcementViewHolder, int i) {
        announcementViewHolder.location.setText("Location: " + announcements.get(i).getLocation().getDescription());
        announcementViewHolder.strollStartTime.setText("Stroll starts at: " + announcements.get(i).getStrollStartTime());
        announcementViewHolder.strollEndTime.setText("Stroll ends at: " + announcements.get(i).getStrollEndTime());
        announcementViewHolder.adEndTime.setText("Announcement ends at: " + announcements.get(i).getAdEndTime());
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }
}