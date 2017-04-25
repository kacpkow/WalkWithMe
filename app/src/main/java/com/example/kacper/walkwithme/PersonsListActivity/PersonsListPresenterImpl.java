//package com.example.kacper.walkwithme.PersonsListActivity;
//
//import com.example.kacper.walkwithme.MainActivity.PersonsList.Person;
//import com.example.kacper.walkwithme.MainActivity.PersonsList.PersonAdapter;
//import com.example.kacper.walkwithme.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by kacper on 2017-04-06.
// */
//
//public class PersonsListPresenterImpl implements PersonsListPresenter {
//    private PersonsListView personsListView;
//    private Person person;
//
//    private List<Person> persons;
//
//    public PersonsListPresenterImpl(PersonsListView personsListView) {
//        this.personsListView = personsListView;
//        this.person = new Person();
//    }
//
//    @Override
//    public void initializeData() {
//        persons = new ArrayList<>();
//        persons.add(new Person(1, "Emma Wilson", "23 years old", "12 km", R.drawable.ic_map));
//        persons.add(new Person(2, "Lavery Maiss", "25 years old", "20 km", R.drawable.ic_map));
//        persons.add(new Person(3, "Lillie Watts", "35 years old", "5 km", R.drawable.ic_map));
//    }
//
//    public void initializeAdapter(){
//        PersonAdapter adapter = new PersonAdapter(persons);
//        personsListView.setAdapter(adapter);
//    }
//}
