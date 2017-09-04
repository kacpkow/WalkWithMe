package com.example.kacper.walkwithme.MainActivity.Chat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kacper.walkwithme.MainActivity.SimpleDividerItemDecoration;
import com.example.kacper.walkwithme.Model.ChatData;
import com.example.kacper.walkwithme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private RecyclerView rv;
    private List<ChatData> chatDataList;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        rv=(RecyclerView)v.findViewById(R.id.rvChat);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        chatDataList = new ArrayList<>();
        initializeData();
        initializeAdapter();
        return v;
    }

    void initializeData(){
        ChatData chatData = new ChatData();
        chatData.setPersonPhoto(null);
        chatData.setName("Marek");

        chatDataList.add(chatData);
    }

    void initializeAdapter(){
        ChatAdapter adapter = new ChatAdapter(chatDataList, this.getContext());
        rv.setAdapter(adapter);
    }

}
