package com.example.kacper.walkwithme.Model;

/**
 * Created by kacper on 2017-08-31.
 */

public class UserAndUserData {

    private int user_id;
    private String nick;
    private String password;
    private String mail;
    private String firstName;
    private String lastName;
    private String city;
    private String date;

    public UserAndUserData()
    {

    }

    public UserAndUserData(User user, UserData userData)
    {
        this.user_id = user.getUser_id();
        this.nick = user.getNick();
        this.password = user.getPassword();
        this.mail = user.getMail();
        this.firstName = userData.getFirstName();
        this.lastName = userData.getLastName();
        this.city = userData.getCity();
        this.date = userData.getDate();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
