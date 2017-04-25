package com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kacper.walkwithme.MainActivity.SimpleDividerItemDecoration;
import com.example.kacper.walkwithme.R;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsFragment extends Fragment {
    private List<ForcomingAppointment> forcomingAppointments;
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointments, container, false);

        rv=(RecyclerView)rootView.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getResources()));

        initializeData();
        initializeAdapter();

        return rootView;
    }

    private void initializeData(){
        forcomingAppointments = new ArrayList<>();
        forcomingAppointments.add(new ForcomingAppointment("Emma Wilson", "Gliwice Park", "27.04.2017 15:00", R.drawable.ic_map));
        forcomingAppointments.add(new ForcomingAppointment("Lavery Maiss", "Katowice Skwer", "29.04.2017 13:15", R.drawable.ic_map));
        forcomingAppointments.add(new ForcomingAppointment("Lillie Watts", "Warszawa Most Lazienkowski", "30.04.2017 18:30", R.drawable.ic_map));
    }

    private void initializeAdapter(){
        ForcomingAppointmentsAdapter adapter = new ForcomingAppointmentsAdapter(forcomingAppointments);
        rv.setAdapter(adapter);
    }
}