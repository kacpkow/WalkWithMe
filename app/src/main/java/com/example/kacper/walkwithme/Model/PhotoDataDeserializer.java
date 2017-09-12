package com.example.kacper.walkwithme.Model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class PhotoDataDeserializer implements JsonDeserializer<PhotoData>{
    @Override
    public PhotoData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final JsonElement jsonPhotoId = jsonObject.get("photoId");
        final int photoId = jsonPhotoId.getAsInt();

        final JsonElement jsonData = jsonObject.get("data");
        final String data = jsonData.getAsString();
        final byte[] dataByte = data.getBytes(Charset.forName("UTF-8"));

        final JsonElement jsonTook_time = jsonObject.get("took_time");
        final String took_time = jsonTook_time.getAsString();


        final PhotoData photo = new PhotoData();
        photo.setPhotoId(photoId);
        photo.setData(dataByte);
        photo.setTook_time(took_time);
        return photo;
    }
}
