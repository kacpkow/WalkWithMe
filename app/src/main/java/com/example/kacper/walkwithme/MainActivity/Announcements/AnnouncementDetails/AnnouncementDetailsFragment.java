package com.example.kacper.walkwithme.MainActivity.Announcements.AnnouncementDetails;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kacper.walkwithme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementDetailsFragment extends Fragment {

    TextView strollLocationView;
    TextView strollStartTimeView;
    TextView strollEndTimeView;
    TextView strollDescriptionView;
    ImageButton showLocationButton;

    Button cancelButton;

    private Double latitude = 0.0;
    private Double longitude = 0.0;

    public AnnouncementDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_announcement_details, container, false);
        showLocationButton = (ImageButton)v.findViewById(R.id.strollLocationButton);
        cancelButton = (Button)v.findViewById(R.id.cancelDetails);

        strollStartTimeView = (TextView) v.findViewById(R.id.strollStartTime);
        strollEndTimeView = (TextView) v.findViewById(R.id.strollEndTime);
        strollLocationView = (TextView) v.findViewById(R.id.strollLocation);
        strollDescriptionView = (TextView) v.findViewById(R.id.strollDescription);

        strollStartTimeView.setText(getArguments().getString("startTime"));
        strollEndTimeView.setText(getArguments().getString("endTime"));
        strollLocationView.setText(getArguments().getString("location"));
        strollDescriptionView.setText(getArguments().getString("description"));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return v;
    }

}
