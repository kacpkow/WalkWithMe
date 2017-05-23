package com.example.kacper.walkwithme.MakeStrollActivity;

/**
 * Created by kacper on 2017-05-23.
 */

public class MakeStrollResponse {
    private String responseContent;

    public MakeStrollResponse(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getResponseContent() {

        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }
}
