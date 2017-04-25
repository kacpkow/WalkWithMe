//package com.example.kacper.walkwithme.PersonsListActivity;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import com.example.kacper.walkwithme.MainActivity.PersonsList.PersonAdapter;
//import com.example.kacper.walkwithme.R;
//import com.example.kacper.walkwithme.MainActivity.SimpleDividerItemDecoration;
//
//public class PersonsListActivity extends AppCompatActivity implements PersonsListView {
//
//    private PersonsListPresenter presenter;
//    private RecyclerView rv;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_persons_list);
//        rv=(RecyclerView)findViewById(R.id.rv);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        rv.setLayoutManager(llm);
//        rv.setHasFixedSize(true);
//        rv.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
//        presenter = new PersonsListPresenterImpl(this);
//        presenter.initializeData();
//        presenter.initializeAdapter();
//
//    }
//
//    public void setAdapter(PersonAdapter adapter){
//        rv.setAdapter(adapter);
//    }
//
//}
