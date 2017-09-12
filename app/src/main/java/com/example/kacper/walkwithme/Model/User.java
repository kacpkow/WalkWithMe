package com.example.kacper.walkwithme.Model;
/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class User {

    private int user_id;
    private String nick;
    private String password;
    private String mail;

    public User(int user_id, String nick, String password, String mail) {
        this.user_id = user_id;
        this.nick = nick;
        this.password = password;
        this.mail = mail;
    }

    public User() {
    }

    public User(UserRegister userRegister) {
        this.nick = userRegister.getNick();
        this.password = userRegister.getPassword();
        this.mail = userRegister.getMail();
    }

    public int getUser_id() {
        return user_id;
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

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
