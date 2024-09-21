package kr.ac.chungbuk.harmonize.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Gson에서 LocalDateTime을 파싱할 수 있도록 Deserializer를 설정한 객체를 제공합니다.
public class GsonDateSupport {

    public static Gson getInstance() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type type,
                                             JsonDeserializationContext jsonDeserializationContext)
                    throws JsonParseException {

                try {
                    return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                } catch (Exception e) {
                    return null;
                }
            }
        });

        return gsonBuilder.create();
    }
}
