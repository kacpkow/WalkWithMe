package com.example.kacper.walkwithme.Model;

/**
 * Created by kacper on 2017-08-28.
 */

public class ChatData {
    private int userId;
    private String firstName;
    private String lastName;
    private String personPhoto;


    public ChatData() {
    }

    public ChatData(int userId, String firstName, String lastName, String personPhoto) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personPhoto = personPhoto;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getPersonPhoto() {
        return personPhoto;
    }

    public void setPersonPhoto(String personPhoto) {
        this.personPhoto = personPhoto;
    }
}
