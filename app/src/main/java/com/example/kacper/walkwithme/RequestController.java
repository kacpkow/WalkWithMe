package com.example.kacper.walkwithme;

import com.example.kacper.walkwithme.LoginActivity.LoginActivity;
import com.example.kacper.walkwithme.LoginActivity.LoginContent;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by kacper on 2017-07-26.
 */

/*Singleton request class*/
public class RequestController {
    private static RequestController mInstance = null;
    CookieJar cookieJar;
    OkHttpClient client;
    public static boolean state = false;

    private RequestController(){
        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(LoginActivity.get()));
        client = new OkHttpClient.Builder().cookieJar(cookieJar).build();
    }

    public static RequestController getInstance(){

        if(mInstance == null)
        {
            mInstance = new RequestController();
        }
        return mInstance;
    }

    public OkHttpClient getClient(){
        return client;
    }

    public void newClient(){
        mInstance = new RequestController();
        state = false;
    }

    public boolean getState(){
        return state;
    }

    public void setState(boolean newState){
        this.state = newState;
    }
}
