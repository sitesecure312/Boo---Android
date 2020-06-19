package com.boo.app.api;


import com.boo.app.ui.utils.DateFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Anton Hadutski on 10/1/14.
 */
public class GsonConfig {

    static JsonDeserializer<DateTime> dateDeser = new JsonDeserializer<DateTime>() {
        @Override
        public DateTime deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
            if (json == null) {
                return null;
            } else {
                if (!json.isJsonNull()) {
                    DateTime dateTime = DateFormat.parseDate(json.getAsString());
                    return dateTime;
                } else {
                    return null;
                }
            }
        }
    };

    static JsonSerializer<DateTime> dateSer = new JsonSerializer<DateTime>() {
        @Override
        public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
            if (src != null) {
                return new JsonPrimitive(DateFormat.getISODateTime(src));
            } else {
                return null;
            }
        }
    };

    public static GsonBuilder getBuilder() {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, dateDeser)
                .registerTypeAdapter(DateTime.class, dateSer);

        return builder;
    }

    public static Gson buildDefaultJson() {
        Gson gson = getBuilder()
                // .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson;
    }

    public static Map<String, String> getValues(Object object) {
        Gson gson = buildDefaultJson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> myMap = gson.fromJson(gson.toJson(object), type);
        return myMap;
    }
}
