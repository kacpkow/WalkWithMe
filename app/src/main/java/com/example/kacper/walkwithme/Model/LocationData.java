package com.example.kacper.walkwithme.Model;

/**
 * Created by kacper on 2017-06-17.
 */

public class LocationData {
    private int location_id;
    private double latitude;
    private double longtitude;
    private String description;

    public LocationData(int location_id, double latitude, double longtitude, String description) {
        this.location_id = location_id;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.description = description;
    }

    public LocationData() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
