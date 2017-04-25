package com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments;

/**
 * Created by kacper on 2017-04-04.
 */

public class ForcomingAppointment {
    String name;
    String location;
    String datetime;
    int photoId;

    ForcomingAppointment(String name, String location, String datetime, int photoId) {
        this.name = name;
        this.location = location;
        this.datetime = datetime;
        this.photoId = photoId;
    }
}
