package com.example.kacper.walkwithme.MainActivity.StrollRequests;

/**
 * Created by kacper on 2017-06-09.
 */

public class StrollRequestContent {

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public StrollRequestContent(Integer userId) {
        this.userId = userId;
    }
}
