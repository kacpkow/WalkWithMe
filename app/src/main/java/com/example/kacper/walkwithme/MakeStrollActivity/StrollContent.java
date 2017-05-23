package com.example.kacper.walkwithme.MakeStrollActivity;

/**
 * Created by kacper on 2017-05-23.
 */

public class StrollContent {
    private Integer currentUserId;
    private Integer userId;
    private String date;
    private String time;
    private String location;

    public StrollContent(Integer currentUserId, Integer userId, String date, String time, String location) {
        this.currentUserId = currentUserId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.location = location;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
