package com.example.kacper.walkwithme.MainActivity.PersonsList;

/**
 * Created by kacper on 2017-04-06.
 */

public class Person {
    Integer id;
    String name;
    Integer age;
    String distance; //distance from user based on maps location
    int photoId;

    Person(Integer id, String name, Integer age, String distance, int photoId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.distance = distance;
        this.photoId = photoId;
    }

    Person(){
        this.id = 0;
        this.name = null;
        this.age = null;
        this.distance = null;
        this.photoId = 0;
    }
}