package com.example.kacper.walkwithme.Model;
/**
 * @author Marek Makowski
 * @version 1.0
 */
public class UserProfileData {
    private int user_id;
    private String nick;
    private String firstName;
    private String lastName;
    private String city;
    private String birth_date;
    private double latitude;
    private double longtitude;
    private String description;
    private String photo_url;
    private String email;

    public UserProfileData(int user_id, String nick, String firstName, String lastName, String city,
                           String birth_date, double latitude, double longtitude, String description, String photo_url, String email) {
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
        this.email = email;
    }

    public UserProfileData() {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
