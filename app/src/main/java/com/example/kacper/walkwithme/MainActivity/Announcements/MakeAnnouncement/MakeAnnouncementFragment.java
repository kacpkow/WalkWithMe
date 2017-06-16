package com.example.kacper.walkwithme.MainActivity.Announcements.MakeAnnouncement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kacper.walkwithme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeAnnouncementFragment extends Fragment {


    public MakeAnnouncementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_make_announcement, container, false);
    }

}
