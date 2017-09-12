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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kacper.walkwithme.Model.Person;
import com.example.kacper.walkwithme.PersonDetails.PersonDetailsFragment;
import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * @author Kacper Kowalik
 * @version 1.0
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
        de.hdodenhof.circleimageview.CircleImageView personMediumPhoto;

        private Context context;

        PersonViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.pv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            distance = (TextView)itemView.findViewById(R.id.person_distance);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personMediumPhoto = (de.hdodenhof.circleimageview.CircleImageView)itemView.findViewById(R.id.person_photo);
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
            args.putInt("USER_ID_1", userId);
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

        try{
            Glide.with(mContext)
                    .load(Base64.decode(persons.get(i).getPhoto_url(), Base64.DEFAULT))
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate())
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
