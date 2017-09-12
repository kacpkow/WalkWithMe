package com.example.kacper.walkwithme.Model;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class SearchCriteria {
    int userId;
    int ageFrom;
    int ageTo;
    Double distance;
    Double userLatitude;
    Double userLongtitude;

    public SearchCriteria(int userId, int ageFrom, int ageTo, double distance, Double userLatitude, Double userLongtitude) {
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

    public int getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(int ageFrom) {
        this.ageFrom = ageFrom;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getUserId() {

        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}