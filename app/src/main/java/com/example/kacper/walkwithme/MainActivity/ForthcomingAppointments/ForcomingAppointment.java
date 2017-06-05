package com.example.kacper.walkwithme.MainActivity.ForthcomingAppointments;

/**
 * Created by kacper on 2017-04-04.
 */

public class ForcomingAppointment {

    Integer strollId;
    Integer userId;
    String firstName;
    String lastName;
    String location;
    String date;
    String time;
    String mediumPhoto;

    public ForcomingAppointment(Integer strollId, Integer userId, String firstName, String lastName, String location, String date, String time, String mediumPhoto) {
        this.strollId = strollId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.date = date;
        this.time = time;
        this.mediumPhoto = mediumPhoto;
    }

    public Integer getStrollId() {
        return strollId;
    }

    public void setStrollId(Integer strollId) {
        this.strollId = strollId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMediumPhoto() {
        return mediumPhoto;
    }

    public void setMediumPhoto(String mediumPhoto) {
        this.mediumPhoto = mediumPhoto;
    }
}
