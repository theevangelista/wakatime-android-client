package com.wakatime.android.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */

public class JsonParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T read(String json, Class<T> target) {
        if (json == null || json.isEmpty()) {
            return null; //garbage in garbage out
        }
        try {
            return mapper.readValue(json, target);
        } catch (IOException e) {
            Timber.e(e, "Error reading json value");
            return null;
        }
    }


    public static <T> T read(String json, TypeReference<T> target) {
        if (json == null || json.isEmpty()) {
            return null; //garbage in garbage out
        }
        try {
            return mapper.readValue(json, target);
        } catch (IOException e) {
            Timber.e(e, "Error reading json value");
            return null;
        }
    }

    public static <T> String write(T object) {
        Timber.d("Writing JSON for %s", object);
        if (object == null) {
            return null; //garbage in garbage out
        }
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            Timber.e(e, "Error processing object into json string");
            return null;
        }
    }
}
