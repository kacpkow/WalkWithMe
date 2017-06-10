package com.example.kacper.walkwithme.MainActivity.StrollRequests;

/**
 * Created by kacper on 2017-06-09.
 */

public class StrollRequest {
    private String requestNr;
    private Integer userId;
    private String firstName;
    private String lastName;
    private String location;
    private String date;
    private String time;
    private String mediumPhoto;

    public StrollRequest(String requestNr, Integer userId, String firstName, String lastName, String location, String date, String time, String mediumPhoto) {
        this.requestNr = requestNr;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.date = date;
        this.time = time;
        this.mediumPhoto = mediumPhoto;
    }

    public String getRequestNr() {
        return requestNr;
    }

    public void setRequestNr(String requestNr) {
        this.requestNr = requestNr;
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
