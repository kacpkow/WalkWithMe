package com.example.kacper.walkwithme.Model;

/**
 * Created by kacper on 2017-08-28.
 */

public class ChatData {
    private String name;
    private byte[] personPhoto;

    public ChatData(String name, byte[] personPhoto) {
        this.name = name;
        this.personPhoto = personPhoto;
    }

    public ChatData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPersonPhoto() {
        return personPhoto;
    }

    public void setPersonPhoto(byte[] personPhoto) {
        this.personPhoto = personPhoto;
    }
}
