package com.example.kacper.walkwithme.RegistrationActivity;

/**
 * Created by kacper on 2017-05-09.
 */

public class RegistrationContent {
    private String nick;
    private String password;
    private String mail;

    public RegistrationContent(String nick, String password, String mail) {
        this.nick = nick;
        this.password = password;
        this.mail = mail;
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
    //    private String firstName;
//    private String lastName;
//    private String city;
}
