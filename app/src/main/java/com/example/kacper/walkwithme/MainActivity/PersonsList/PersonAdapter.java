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

import com.example.kacper.walkwithme.PersonDetails.PersonDetailsActivity;
import com.example.kacper.walkwithme.R;

import java.util.List;

/**
 * Created by kacper on 2017-04-06.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {


    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView personName;
        TextView personAge;
        TextView distance;
        ImageView personPhoto;

        private Context context;

        PersonViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.pv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            distance = (TextView)itemView.findViewById(R.id.person_distance);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            //itemView.setOnClickListener(this);
      }

        @Override
        public void onClick(View v) {
            context = v.getContext();
            Intent intent = new Intent(context, PersonDetailsActivity.class);
            intent.putExtra("id", getAdapterPosition());
            context.startActivity(intent);
        }
    }

    List<Person> persons;

    PersonAdapter(List<Person> persons){
        this.persons = persons;
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
        personViewHolder.personName.setText(persons.get(i).name);
        personViewHolder.personAge.setText(persons.get(i).age.toString() + " years");
        personViewHolder.distance.setText(persons.get(i).distance);
        personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
    }


    @Override
    public int getItemCount() {
        return persons.size();
    }
}
