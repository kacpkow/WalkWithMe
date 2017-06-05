package com.example.kacper.walkwithme.MakeStrollActivity;

/**
 * Created by kacper on 2017-05-23.
 */

public class StrollContent {
    private Integer currentUserId;
    private Integer userId;
    private String date;
    private String time;
    private String locationName;
    private double longitude;
    private double latitude;



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
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

    public StrollContent(Integer currentUserId, Integer userId, String date, String time, String locationName, double longitude, double latitude) {
        this.currentUserId = currentUserId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLocationName() {

        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
