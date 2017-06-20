package com.example.kacper.walkwithme.Model;

/**
 * Created by kacper on 2017-05-23.
 */

public class UserProfileData {
    private int user_id;
    private String nick;
    private String firstName;
    private String lastName;
    private String city;
    private String birth_date;
    private Double latitude;
    private Double longtitude;
    private String description;
    private String photo_url;

    public UserProfileData(int user_id, String nick, String firstName, String lastName, String city,
                           String birth_date, Double latitude, Double longtitude, String description, String photo_url) {
        this.user_id = user_id;
        this.nick = nick;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.birth_date = birth_date;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.description = description;
        this.photo_url = photo_url;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}
