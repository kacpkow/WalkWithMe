package com.example.kacper.walkwithme.Model;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class AdvertisementData {

    private Integer adId;
    private Integer userId;
    private LocationData location;
    private String description;
    private String strollStartTime;
    private String strollEndTime;
    private String adEndTime;
    private String privacy;

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocationData getLocation() {
        return location;
    }

    public void setLocation(LocationData location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStrollStartTime() {
        return strollStartTime;
    }

    public void setStrollStartTime(String strollStartTime) {
        this.strollStartTime = strollStartTime;
    }

    public String getStrollEndTime() {
        return strollEndTime;
    }

    public void setStrollEndTime(String strollEndTime) {
        this.strollEndTime = strollEndTime;
    }

    public String getAdEndTime() {
        return adEndTime;
    }

    public void setAdEndTime(String adEndTime) {
        this.adEndTime = adEndTime;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}
