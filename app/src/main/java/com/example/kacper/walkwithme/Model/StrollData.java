package com.example.kacper.walkwithme.Model;

/**
 * Created by kacper on 2017-06-20.
 */

public class StrollData {

    private int strollId;
    private LocationData location;
    private String info;
    private String data_start;
    private String data_end;
    private String status;
    private String privacy;
    private int user;

    public StrollData() {
    }

    public StrollData(int strollId, LocationData location, String info, String data_start, String data_end, String status, String privacy, int user) {
        this.strollId = strollId;
        this.location = location;
        this.info = info;
        this.data_start = data_start;
        this.data_end = data_end;
        this.status = status;
        this.privacy = privacy;
        this.user = user;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getData_start() {
        return data_start;
    }

    public void setData_start(String data_start) {
        this.data_start = data_start;
    }

    public String getData_end() {
        return data_end;
    }

    public void setData_end(String data_end) {
        this.data_end = data_end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public LocationData getLocation() {
        return location;
    }

    public void setLocation(LocationData location) {
        this.location = location;
    }

    public int getStrollId() {
        return strollId;
    }

    public void setStrollId(int strollId) {
        this.strollId = strollId;
    }
}
