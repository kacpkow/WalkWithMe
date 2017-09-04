package com.example.kacper.walkwithme.Model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Created by kacper on 2017-09-03.
 */

public class UserProfileDataDeserializer implements JsonDeserializer<UserProfileData> {
    @Override
    public UserProfileData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final JsonElement jsonUserId = jsonObject.get("user_id");
        final int userId = jsonUserId.getAsInt();

        final JsonElement jsonNick = jsonObject.get("nick");
        final String nick = jsonNick.getAsString();

        final JsonElement jsonFirstName = jsonObject.get("firstName");
        final String firstName = jsonFirstName.getAsString();

        final JsonElement jsonLastName = jsonObject.get("lastName");
        final String lastName = jsonLastName.getAsString();

        final JsonElement jsonCity = jsonObject.get("city");
        final String city= jsonCity.getAsString();

        final JsonElement jsonBirthDate = jsonObject.get("birth_date");
        final String birthDate = jsonBirthDate.getAsString();

        final JsonElement jsonLatitude = jsonObject.get("latitude");
        final Double latitude = jsonLatitude.getAsDouble();

        final JsonElement jsonLongtitude = jsonObject.get("longtitude");
        final Double longtitude = jsonLongtitude.getAsDouble();

        final JsonElement jsonDescription = jsonObject.get("description");
        final String description = jsonDescription.getAsString();

        final JsonElement jsonPhotoUrl = jsonObject.get("photo_url");
        final String photoUrl = jsonPhotoUrl.getAsString();
        //final byte[] photoUrlByte = photoUrl.getBytes(Charset.forName("UTF-8"));

        final JsonElement jsonEmail = jsonObject.get("email");
        final String email = jsonEmail.getAsString();


        final UserProfileData user = new UserProfileData();

        user.setDescription(description);
        user.setPhoto_url(photoUrl);
        user.setNick(nick);
        user.setFirstName(firstName);
        user.setBirth_date(birthDate);
        user.setLatitude(latitude);
        user.setLongtitude(longtitude);
        user.setCity(city);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUser_id(userId);

        return user;
    }
}
