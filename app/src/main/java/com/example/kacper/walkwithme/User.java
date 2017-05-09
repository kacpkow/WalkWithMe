package com.example.kacper.walkwithme;

/**
 * Created by kacper on 2017-04-22.
 */

public class User {
    private String nick;
    private String firstName;
    private String lastName;
    //private Double geoLat;  // user location longtitude
    //private Double geoLng;  // user location latitude
    private String email;
    private String sessionId;

    public User(String nick, String firstName, String lastName, String email, String sessionId) {
        this.nick = nick;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sessionId = sessionId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
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

//    public Double getGeoLat() {
//        return geoLat;
//    }
//
//    public void setGeoLat(Double geoLat) {
//        this.geoLat = geoLat;
//    }
//
//    public Double getGeoLng() {
//        return geoLng;
//    }
//
//    public void setGeoLng(Double geoLng) {
//        this.geoLng = geoLng;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
