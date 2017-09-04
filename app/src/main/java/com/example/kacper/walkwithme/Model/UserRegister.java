package com.example.kacper.walkwithme.Model;

/**
 * Created by kacper on 2017-08-31.
 */

public class UserRegister {

    private String nick;
    private String password;
    private String mail;

    public UserRegister(String nick, String password, String mail) {

        this.nick = nick;
        this.password = password;
        this.mail = mail;
    }

    public UserRegister() {
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

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
