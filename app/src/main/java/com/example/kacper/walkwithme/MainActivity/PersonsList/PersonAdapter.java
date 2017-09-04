package com.example.kacper.walkwithme.MainActivity.PersonsList;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kacper.walkwithme.Model.Person;
import com.example.kacper.walkwithme.PersonDetails.PersonDetailsFragment;
import com.example.kacper.walkwithme.R;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.util.List;

/**
 * Created by kacper on 2017-04-06.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
    private Context mContext;
    private List<Person> persons;

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView personName;
        TextView personAge;
        int userId;
        int currentUserId;
        String personDescription;
        String personFirstName;
        String personLastName;
        String personLocation;
        String personPhoto;
        TextView distance;
        ImageView personMediumPhoto;

        private Context context;

        PersonViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.pv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            distance = (TextView)itemView.findViewById(R.id.person_distance);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personMediumPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            personLocation = "";
            personFirstName = "";
            personLastName = "";
            userId = 0;
            personDescription = "";
            itemView.setOnClickListener(this);

      }

        @Override
        public void onClick(View v) {

            FragmentManager fm = ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PersonDetailsFragment newFragment = new PersonDetailsFragment();
            Fragment f = ((AppCompatActivity)v.getContext()).getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            Bundle args = new Bundle();
            args.putInt("USER_ID", userId);
            args.putString("USER_AGE", personAge.getText().toString());
            args.putString("USER_LOCATION", personLocation);
            args.putString("USER_DESCRIPTION", personDescription);
            args.putString("USER_NAME", personName.getText().toString());
            args.putString("USER_IMAGE", personPhoto);

            newFragment.setArguments(args);

            ft.replace(R.id.fragment_container, newFragment);
            ft.addToBackStack(null);
            ft.commit();

        }
    }

    PersonAdapter(List<Person> persons, Context context){
        this.persons = persons;
        mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.person_item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, final int i) {
        personViewHolder.personName.setText(persons.get(i).getFirstName() + " "+persons.get(i).getLastName());
        personViewHolder.personAge.setText(persons.get(i).getAge().toString() + " years");
        personViewHolder.distance.setText(persons.get(i).getDistance() + " km");
        personViewHolder.userId = persons.get(i).getUser_id();
        personViewHolder.personFirstName = persons.get(i).getFirstName();
        personViewHolder.personLastName = persons.get(i).getLastName();
        personViewHolder.personDescription = persons.get(i).getDescription();
        personViewHolder.personLocation = persons.get(i).getCity();
        personViewHolder.personPhoto = persons.get(i).getPhoto_url();
//        Glide.with(mContext).load(persons.get(i).getPhoto_url())
//                .into(personViewHolder.personMediumPhoto);

//        Log.e("photo",  String.valueOf(persons.get(i).getPhoto_url().toString().getBytes(StandardCharsets.UTF_8)));
        //Log.e("photo",  String.valueOf(persons.get(i).getPhoto_url().toString()));

        try{
//            Log.e("photo1",  array.toString());
            Glide.with(mContext)
                    .load(Base64.decode(persons.get(i).getPhoto_url(), Base64.DEFAULT))
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(personViewHolder.personMediumPhoto);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}
