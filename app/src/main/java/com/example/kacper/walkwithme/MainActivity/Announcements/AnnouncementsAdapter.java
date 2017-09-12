package com.example.kacper.walkwithme.MainActivity.Announcements;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kacper.walkwithme.MainActivity.Announcements.AnnouncementDetails.AnnouncementDetailsFragment;
import com.example.kacper.walkwithme.MainActivity.Announcements.EditAnnouncement.EditAnnouncementFragment;
import com.example.kacper.walkwithme.Model.AdvertisementData;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementViewHolder>{
    Integer userId;
    private Context mContext;
    OkHttpClient client;

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView locationView;
        TextView strollStartTime;
        TextView strollEndTime;
        TextView adEndTime;
        Button detailsButton;
        Button editButton;
        Button deleteButton;
        Integer adId;
        Integer position;

        String advEndTime;

        String description;
        String startTime;
        String endTime;
        String location;
        Integer userId;
        String privacy;
        Integer locationId;
        Double latidute;
        Double longtitude;

        private Context context;

        AnnouncementViewHolder(final View itemView) {
            super(itemView);
            client = RequestController.getInstance().getClient();
            cv = (CardView)itemView.findViewById(R.id.cv_announcements);
            locationView = (TextView)itemView.findViewById(R.id.announcement_location_label);
            strollStartTime = (TextView)itemView.findViewById(R.id.announcement_stroll_start_time);
            strollEndTime = (TextView)itemView.findViewById(R.id.announcement_stroll_end_time);
            adEndTime = (TextView)itemView.findViewById(R.id.announcement_ad_end_time);
            detailsButton = (Button)itemView.findViewById(R.id.announcement_details_Button);
            editButton = (Button)itemView.findViewById(R.id.announcement_edit_Button);
            deleteButton = (Button)itemView.findViewById(R.id.announcement_delete_Button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());

                    dialogBuilder.setPositiveButton("DELETE ANNOUNCEMENT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAnnouncement(adId, position);
                    }
                    });
                    dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.setTitle("Are you sure you want to delete this announcement?");
                    alertDialog.show();
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    EditAnnouncementFragment newFragment = new EditAnnouncementFragment();
                    Fragment f = ((AppCompatActivity)mContext).getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                    Bundle args = new Bundle();
                    args.putInt("adId", adId);
                    args.putInt("userId", userId);
                    args.putString("privacy", privacy);
                    args.putInt("locationId", locationId);
                    args.putDouble("latitude", latidute);
                    args.putDouble("longtitude", longtitude);
                    args.putString("startTime", startTime);
                    args.putString("endTime", endTime);
                    args.putString("location", location);
                    args.putString("description", description);
                    newFragment.setArguments(args);

                    ft.replace(R.id.fragment_container, newFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    AnnouncementDetailsFragment newFragment = new AnnouncementDetailsFragment();
                    Fragment f = ((AppCompatActivity)mContext).getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                    Bundle args = new Bundle();
                    args.putInt("adId", adId);
                    args.putInt("userId", userId);
                    args.putString("privacy", privacy);
                    args.putInt("locationId", locationId);
                    args.putDouble("latitude", latidute);
                    args.putDouble("longtitude", longtitude);
                    args.putString("startTime", startTime);
                    args.putString("endTime", endTime);
                    args.putString("advEndTime", advEndTime);
                    args.putString("location", location);
                    args.putString("description", description);
                    newFragment.setArguments(args);

                    ft.replace(R.id.fragment_container, newFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }

    }

    private void deleteAnnouncement(Integer adId, final Integer position){
        String url = mContext.getString(R.string.service_address) + "adv/"+adId.toString();

        final Request request;
        request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", "error while connectinh with server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("body", response.body().string());
                advertisementDatas.remove(position);
                presenter.refreshElements();
            }
        });
    }

    List<AdvertisementData> advertisementDatas;
    AnnouncementsPresenter presenter;

    AnnouncementsAdapter(List<AdvertisementData> advertisementDatas, Context mContext, Integer userId, AnnouncementsPresenter presenter){
        this.advertisementDatas = advertisementDatas;
        this.mContext = mContext;
        this.userId = userId;
        this.presenter = presenter;
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
        announcementViewHolder.locationView.setText("Location: " + advertisementDatas.get(i).getLocation().getDescription());
        announcementViewHolder.strollStartTime.setText("Stroll starts at: " + advertisementDatas.get(i).getStrollStartTime());
        announcementViewHolder.strollEndTime.setText("Stroll ends at: " + advertisementDatas.get(i).getStrollEndTime());
        announcementViewHolder.adEndTime.setText("Advertisement ends at: " + advertisementDatas.get(i).getAdEndTime());
        announcementViewHolder.adId = advertisementDatas.get(i).getAdId();
        announcementViewHolder.userId = advertisementDatas.get(i).getUserId();
        announcementViewHolder.privacy= advertisementDatas.get(i).getPrivacy();
        announcementViewHolder.locationId = advertisementDatas.get(i).getLocation().getLocation_id();
        announcementViewHolder.latidute = advertisementDatas.get(i).getLocation().getLatitude();
        announcementViewHolder.longtitude = advertisementDatas.get(i).getLocation().getLongtitude();
        announcementViewHolder.description = advertisementDatas.get(i).getDescription();
        announcementViewHolder.startTime = advertisementDatas.get(i).getStrollStartTime();
        announcementViewHolder.endTime = advertisementDatas.get(i).getStrollEndTime();
        announcementViewHolder.advEndTime = advertisementDatas.get(i).getAdEndTime();
        announcementViewHolder.location = advertisementDatas.get(i).getLocation().getDescription();
        announcementViewHolder.position = i;
        if(advertisementDatas.get(i).getUserId() == userId){
            announcementViewHolder.editButton.setVisibility(View.VISIBLE);
            announcementViewHolder.deleteButton.setVisibility(View.VISIBLE);
            announcementViewHolder.detailsButton.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return advertisementDatas.size();
    }
}