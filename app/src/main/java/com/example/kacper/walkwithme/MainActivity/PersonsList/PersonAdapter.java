package com.example.kacper.walkwithme.MainActivity.PersonsList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kacper.walkwithme.PersonDetails.PersonDetailsActivity;
import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * Created by kacper on 2017-04-06.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
    private Context mContext;
    private List<com.example.kacper.walkwithme.MainActivity.PersonsList.Person> persons;

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView personName;
        TextView personAge;
        int userId;
        String personDescription;
        String personFirstName;
        String personLastName;
        String personLocation;
        String personLargePhoto;
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
            personLargePhoto = "";
            personLocation = "";
            personFirstName = "";
            personLastName = "";
            userId = 0;
            personDescription = "";
            itemView.setOnClickListener(this);
      }

        @Override
        public void onClick(View v) {
            context = v.getContext();
            Intent intent = new Intent(context, PersonDetailsActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_AGE", personAge.getText().toString());
            intent.putExtra("USER_LOCATION", personLocation);
            intent.putExtra("USER_DESCRIPTION", personDescription);
            intent.putExtra("USER_FIRST_NAME", personFirstName);
            intent.putExtra("USER_LAST_NAME", personLastName);
            intent.putExtra("USER_IMAGE", personLargePhoto);

            context.startActivity(intent);

        }
    }


    PersonAdapter(List<com.example.kacper.walkwithme.MainActivity.PersonsList.Person> persons, Context context){
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
        personViewHolder.userId = persons.get(i).getId();
        personViewHolder.personFirstName = persons.get(i).getFirstName();
        personViewHolder.personLastName = persons.get(i).getLastName();
        personViewHolder.personDescription = persons.get(i).getPersonDescription();
        personViewHolder.personLocation = persons.get(i).getCity();
        personViewHolder.personLargePhoto = persons.get(i).getLargeImage();
        Glide.with(mContext).load(persons.get(i).getMediumImage())
                .into(personViewHolder.personMediumPhoto);
    }


    @Override
    public int getItemCount() {
        return persons.size();
    }
}
