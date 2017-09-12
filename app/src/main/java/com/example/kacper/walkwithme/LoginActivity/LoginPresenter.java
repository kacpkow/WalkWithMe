package com.example.kacper.walkwithme.LoginActivity;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public interface LoginPresenter<T extends LoginView> {
    void validateCredentials(String username, String password);
    void onDestroy();
}
