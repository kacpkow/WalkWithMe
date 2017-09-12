package com.example.kacper.walkwithme.LoginActivity;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class LoginContent {
    private String username;
    private String password;

    public LoginContent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
