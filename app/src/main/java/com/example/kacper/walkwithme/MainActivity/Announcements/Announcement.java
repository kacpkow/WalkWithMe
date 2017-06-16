package com.example.kacper.walkwithme.MainActivity.Announcements;

/**
 * Created by kacper on 2017-06-13.
 */

public class Announcement {

    public class Location{
        private Integer locationId;
        private Double latidute;
        private Double longtitude;
        private String description;

        public Location(Integer locationId, Double latidute, Double longtitude, String description) {
            this.locationId = locationId;
            this.latidute = latidute;
            this.longtitude = longtitude;
            this.description = description;
        }

        public Integer getLocationId() {
            return locationId;
        }

        public void setLocationId(Integer locationId) {
            this.locationId = locationId;
        }

        public Double getLatidute() {
            return latidute;
        }

        public void setLatidute(Double latidute) {
            this.latidute = latidute;
        }

        public Double getLongtitude() {
            return longtitude;
        }

        public void setLongtitude(Double longtitude) {
            this.longtitude = longtitude;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    private Integer adId;
    private Integer userId;
    private Location location;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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
