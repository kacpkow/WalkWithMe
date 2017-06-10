package com.example.kacper.walkwithme.MainActivity.StrollRequests;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments.ForcomingAppointment;
import com.example.kacper.walkwithme.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by kacper on 2017-06-09.
 */

public class StrollRequestsAdapter extends RecyclerView.Adapter<StrollRequestsAdapter.StrollRequestViewHolder> {
    private Context mContext;

    public static class StrollRequestViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView personName;
        TextView locationView;
        TextView datetime;
        ImageView personPhoto;
        ImageButton acceptButton;
        ImageButton declineButton;

        String requestNr;
        Integer userId;
        String firstName;
        String lastName;
        String location;
        String date;
        String time;
        String mediumPhoto;

        private Context context;

        StrollRequestViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.sr_person_name);
            locationView = (TextView)itemView.findViewById(R.id.sr_location);
            datetime = (TextView)itemView.findViewById(R.id.sr_datetime);
            personPhoto = (ImageView)itemView.findViewById(R.id.sr_person_photo);
            acceptButton = (ImageButton)itemView.findViewById(R.id.buttonAccept);
            declineButton = (ImageButton)itemView.findViewById(R.id.buttonDecline);
        }
    }

    public List<StrollRequest> getStrollRequestList() {
        return strollRequestList;
    }

    public void setStrollRequestList(List<StrollRequest> strollRequestList) {
        this.strollRequestList = strollRequestList;
    }

    List<StrollRequest> strollRequestList;

    StrollRequestsAdapter(List<StrollRequest> strollRequests, Context mContext){
        this.strollRequestList = strollRequests;
        this.mContext = mContext;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public StrollRequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stroll_request_item, viewGroup, false);
        StrollRequestViewHolder srvh = new StrollRequestViewHolder(v);
        return srvh;
    }

    @Override
    public void onBindViewHolder(final StrollRequestViewHolder strollRequestViewHolder, final int i) {
        strollRequestViewHolder.personName.setText(strollRequestList.get(i).getFirstName() + " " + strollRequestList.get(i).getLastName());
        strollRequestViewHolder.locationView.setText(strollRequestList.get(i).getLocation());
        strollRequestViewHolder.datetime.setText(strollRequestList.get(i).getDate() + " at " + strollRequestList.get(i).getTime());
        Glide.with(mContext).load(strollRequestList.get(i).getMediumPhoto())
                .into(strollRequestViewHolder.personPhoto);
        strollRequestViewHolder.userId = strollRequestList.get(i).getUserId();
        strollRequestViewHolder.firstName = strollRequestList.get(i).getFirstName();
        strollRequestViewHolder.lastName = strollRequestList.get(i).getLastName();
        strollRequestViewHolder.location= strollRequestList.get(i).getLocation();
        strollRequestViewHolder.date = strollRequestList.get(i).getDate();
        strollRequestViewHolder.time = strollRequestList.get(i).getTime();
        strollRequestViewHolder.mediumPhoto = strollRequestList.get(i).getMediumPhoto();
        strollRequestViewHolder.requestNr = strollRequestList.get(i).getRequestNr();
        strollRequestViewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptStroll(strollRequestViewHolder.requestNr);
                strollRequestList.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, strollRequestList.size());
            }
        });

        strollRequestViewHolder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelStroll(strollRequestViewHolder.requestNr);
                strollRequestList.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, strollRequestList.size());
            }
        });
    }

    public void acceptStroll(String requestId){

        String url ="http://10.0.2.2:8080/acceptStroll";
        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();

        String reqId = requestId;
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(reqId));

        final Request request;
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

            }
        });

    }


    public void cancelStroll(String requestId){

        String url ="http://10.0.2.2:8080/rejectStroll";
        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();

        String reqId = requestId;
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(reqId));

        final Request request;
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

            }
        });

    }

    @Override
    public int getItemCount() {
        return strollRequestList.size();
    }
}