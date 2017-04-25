package com.example.kacper.walkwithme.MainActivity.PersonsList;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments.ForcomingAppointment;
import com.example.kacper.walkwithme.MainActivity.SimpleDividerItemDecoration;
import com.example.kacper.walkwithme.R;

import java.util.ArrayList;
import java.util.List;

public class PersonsListFragment extends Fragment {
    private List<Person> persons;
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_persons_list, container, false);

        rv=(RecyclerView)rootView.findViewById(R.id.rvPersons);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getResources()));

        initializeData();
        initializeAdapter();

        return rootView;
    }

    private void initializeData(){
        persons = new ArrayList<>();
        persons.add(new Person(1,  "Amy Wilson", 23, "15km", R.drawable.ic_map));
        persons.add(new Person(1,  "Amy Wilson", 23, "15km", R.drawable.ic_map));
        persons.add(new Person(1,  "Amy Wilson", 23, "15km", R.drawable.ic_map));
    }

    private void initializeAdapter(){
        PersonAdapter adapter = new PersonAdapter(persons);
        rv.setAdapter(adapter);
    }

}
