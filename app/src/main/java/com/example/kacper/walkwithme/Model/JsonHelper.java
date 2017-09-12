package com.example.kacper.walkwithme.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class JsonHelper {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private static final Type TT_mapIntegerObject = new TypeToken<Map<Integer,MessageNotifications>>(){}.getType();

    public static Map<Integer, MessageNotifications> jsonToMapIntegerObject(String json) {
        Map<Integer, MessageNotifications> ret = new HashMap<Integer, MessageNotifications>();
        if (json == null || json.isEmpty())
            return ret;
        return gson.fromJson(json, TT_mapIntegerObject);
    }
    public static String mapStringObjectToJson(Map<Integer, MessageNotifications> map) {
        if (map == null)
            map = new HashMap<Integer, MessageNotifications>();
        return gson.toJson(map);
    }
}
