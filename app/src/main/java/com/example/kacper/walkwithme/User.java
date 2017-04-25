package com.example.kacper.walkwithme;

/**
 * Created by kacper on 2017-04-22.
 */

public class User {
    private String name;
    private String firstName;
    private String lastName;
    private Double geoLat;  // user location longtitude
    private Double geoLng;  // user location latitude
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(Double geoLat) {
        this.geoLat = geoLat;
    }

    public Double getGeoLng() {
        return geoLng;
    }

    public void setGeoLng(Double geoLng) {
        this.geoLng = geoLng;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
