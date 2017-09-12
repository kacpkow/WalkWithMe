package com.example.kacper.walkwithme.Model;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class RegistrationForm {


    private String nick;

    private String password;

    private String mail;

    private String firstName;

    private String lastName;

    private String city;

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }

    public String getMail() {
        return mail;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public UserAndUserData toUserAndUserData(int userId)
    {
        UserAndUserData result = new UserAndUserData();
        result.setUser_id(userId);
        result.setNick(this.nick);
        result.setPassword(this.password);
        result.setMail(this.mail);
        result.setFirstName(this.firstName);
        result.setLastName(this.lastName);
        result.setCity(this.city);
        result.setDate(this.date);
        return result;

    }
}
