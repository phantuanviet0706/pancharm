package com.example.pancharm.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonConfigUtil {
    static ObjectMapper mapper = new ObjectMapper();

    /**
     * @desc Get String from JSON text
     * @param jsonConfig
     * @param fieldName
     * @return String
     */
    public static String getString(String jsonConfig, String fieldName) {
        try {
            JsonNode node = mapper.readTree(jsonConfig);
            return node.path(fieldName).asText(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @desc Get boolean value from JSON text
     * @param jsonConfig
     * @param fieldName
     * @param defaultValue
     * @return boolean
     */
    public static boolean getBoolean(String jsonConfig, String fieldName, boolean defaultValue) {
        try {
            JsonNode node = mapper.readTree(jsonConfig);
            return node.path(fieldName).asBoolean(defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @desc Get Integer value from JSON text
     * @param jsonConfig
     * @param fieldName
     * @param defaultValue
     * @return int
     */
    public static int getInt(String jsonConfig, String fieldName, int defaultValue) {
        try {
            JsonNode node = mapper.readTree(jsonConfig);
            return node.path(fieldName).asInt(defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @desc Get Json Node from JSON text
     * @param jsonConfig
     * @param fieldName
     * @return JsonNode
     */
    public static JsonNode getNode(String jsonConfig, String fieldName) {
        try {
            JsonNode node = mapper.readTree(jsonConfig);
            return node.path(fieldName);
        } catch (Exception e) {
            return mapper.createObjectNode();
        }
    }
}
