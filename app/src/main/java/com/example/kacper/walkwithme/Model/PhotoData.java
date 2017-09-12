package com.example.kacper.walkwithme.Model;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */
public class PhotoData {
    private int photoId;
    private byte[] data;
    private String took_time;

    public PhotoData() {
    }

    public PhotoData(int photoId, byte[] data, String took_time) {
        this.photoId = photoId;
        this.data = data;
        this.took_time = took_time;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getTook_time() {
        return took_time;
    }

    public void setTook_time(String took_time) {
        this.took_time = took_time;
    }
}
