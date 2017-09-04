package com.example.kacper.walkwithme.MainActivity.Conversation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.kacper.walkwithme.MainActivity.SimpleDividerItemDecoration;
import com.example.kacper.walkwithme.Model.UserMessageData;
import com.example.kacper.walkwithme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment {

    private RecyclerView rv;
    private List<UserMessageData> userMessageDataList;
    EditText newMessageText;
    ImageButton sendButton;

    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_conversation, container, false);
        rv=(RecyclerView)v.findViewById(R.id.rvConversation);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(false);
        newMessageText = (EditText)v.findViewById(R.id.edit_text_conversation);
        sendButton = (ImageButton)v.findViewById(R.id.sendIcon);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMessageText.setText("");
            }
        });

        userMessageDataList = new ArrayList<>();

        initializeAdapter();
        return v;
    }

    void initializeAdapter(){
        ConversationAdapter adapter = new ConversationAdapter(userMessageDataList, this.getContext());
        rv.setAdapter(adapter);
        rv.smoothScrollToPosition(rv.getAdapter().getItemCount());
    }

}
