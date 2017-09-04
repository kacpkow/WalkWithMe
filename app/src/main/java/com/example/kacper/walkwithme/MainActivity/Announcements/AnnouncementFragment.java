package com.example.kacper.walkwithme.MainActivity.Announcements;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.kacper.walkwithme.MainActivity.Announcements.MakeAnnouncement.MakeAnnouncementFragment;
import com.example.kacper.walkwithme.MainActivity.SimpleDividerItemDecoration;
import com.example.kacper.walkwithme.Model.AdvertisementData;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnnouncementFragment extends Fragment implements AnnouncementsView {
    private RecyclerView rv;
    private Integer userId;
    private List<AdvertisementData> advertisementDataList;
    private Spinner spinner;
    private Integer selection;
    private FloatingActionButton addAnnouncementButton;
    private Button searchButton;
    private AnnouncementsAdapter adapter;
    private AnnouncementsPresenter presenter;

    OkHttpClient client;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_announcement, container, false);

        client = RequestController.getInstance().getClient();

        rv = (RecyclerView) rootView.findViewById(R.id.rvAnnouncements);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getResources()));

        advertisementDataList = new ArrayList<>();
        selection = 0;

        searchButton = (Button) rootView.findViewById(R.id.searchWithCriteriaButton);
        spinner = (Spinner) rootView.findViewById(R.id.announcementsTypeSpinner);
        final Animation animScaleButton = AnimationUtils.loadAnimation(getContext(), R.anim.anim_press_menu_button);
        addAnnouncementButton = (FloatingActionButton) rootView.findViewById(R.id.fabAddAnnouncement);
        addAnnouncementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleButton);
                animScaleButton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        showAnnouncementDialog();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        if (spinner != null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selection = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        SharedPreferences settings = this.getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = settings.getInt("ID", 0);

        presenter = new AnnouncementsPresenterImpl(this);

        initializeData();
        initializeAdapter();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertisementDataList.clear();
                initializeData();
                initializeAdapter();
            }
        });

        return rootView;

    }

    private void initializeAdapter() {
        adapter = new AnnouncementsAdapter(advertisementDataList, this.getContext(), userId, presenter);
        rv.setAdapter(adapter);
    }

    public void backgroundThreadInitializeAdapter(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void showAnnouncementDialog() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MakeAnnouncementFragment newFragment = new MakeAnnouncementFragment();
        Fragment f = getFragmentManager().findFragmentById(R.id.fragment_container);
        ft.replace(R.id.fragment_container, newFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public void initializeData() {
        String url;
        Log.e("selection", selection.toString());

        if (selection == 0) {
            url = "http://10.0.2.2:8080/adv";
        } else if (selection == 1) {
            url = "http://10.0.2.2:8080/adv/all";
        } else {
            url = "http://10.0.2.2:8080/adv/friends";
        }

        Gson gson = new Gson();
        MediaType mediaType = MediaType.parse("application/json");

        final Request request;
        request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", "error while connectinh with server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResponse = response.body().string();

                if (jsonResponse != null) {
                    Gson objGson = new GsonBuilder().setPrettyPrinting().create();
                    Type listType = new TypeToken<List<AdvertisementData>>() {
                    }.getType();

                    try {
                        List<AdvertisementData> readFromJson = objGson.fromJson(jsonResponse, listType);
                        if (readFromJson != null) {
                            for (AdvertisementData advertisementDataData : readFromJson
                                    ) {
                                AdvertisementData advertisementData = new AdvertisementData();
                                advertisementData.setLocation(advertisementDataData.getLocation());
                                advertisementData.setStrollStartTime(advertisementDataData.getStrollStartTime());
                                advertisementData.setStrollEndTime(advertisementDataData.getStrollEndTime());
                                advertisementData.setUserId(advertisementDataData.getUserId());
                                advertisementData.setAdId(advertisementDataData.getAdId());
                                advertisementData.setAdEndTime(advertisementDataData.getAdEndTime());
                                advertisementData.setDescription(advertisementDataData.getDescription());
                                advertisementData.setPrivacy(advertisementDataData.getPrivacy());

                                advertisementDataList.add(advertisementData);
                            }
                            backgroundThreadInitializeAdapter(getActivity().getApplicationContext());
                        }

                    } catch (JsonSyntaxException e) {
                        Log.e("error", "error in syntax in returning json");
                    }
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}