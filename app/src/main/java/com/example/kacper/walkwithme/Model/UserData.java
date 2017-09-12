package com.example.kacper.walkwithme.Model;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class UserData {

    private int user_id;
    private String firstName;
    private String lastName;
    private String city;
    private String date;

    public UserData(int user_id, String firstName, String lastName, String city, String date) {
        this.user_id = user_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.date = date;
    }

    public UserData() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserId() {
        return user_id;
    }

    public void setId(int id) {
        this.user_id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String country) {
        this.city = country;
    }

    @Override
    public String toString() {
        return "id=" + user_id + ", firstName=" + firstName + ", lastName=" + lastName + ", country=" + city + ", date=" + date;
    }
}
