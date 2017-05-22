package com.example.kacper.walkwithme.MainActivity.PersonDetails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kacper.walkwithme.R;


public class PersonDetailsFragment extends Fragment {
    private TextView intentName;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Integer id = getArguments().getInt("USER_ID", 0);
        View rootView = inflater.inflate(R.layout.fragment_person_details, container, false);
        //intentName = (TextView)rootView.findViewById(R.id.intentNr1);
        //intentName.setText(id.toString());

        return rootView;
    }

}