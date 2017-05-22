package com.example.kacper.walkwithme.MainActivity.PersonsList;

/**
 * Created by kacper on 2017-05-22.
 */

public class SearchContent {
    Integer userId;
    String ageFrom;
    String ageTo;
    String distance;
    Double userLatitude;
    Double userLongtitude;

    public SearchContent(Integer userId, String ageFrom, String ageTo, String distance, Double userLatitude, Double userLongtitude) {
        this.userId = userId;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.distance = distance;
        this.userLatitude = userLatitude;
        this.userLongtitude = userLongtitude;
    }

    public Double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(Double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public Double getUserLongtitude() {
        return userLongtitude;
    }

    public void setUserLongtitude(Double userLongtitude) {
        this.userLongtitude = userLongtitude;
    }

    public String getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(String ageFrom) {
        this.ageFrom = ageFrom;
    }

    public String getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(String ageTo) {
        this.ageTo = ageTo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Integer getUserId() {

        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
