package com.example.pancharm.util;

import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyUtil {
    protected String DEFAULT_DATA_JSON_PATH = "default-data.json";

    public JSONObject getCompanyDefaultInfo() {
        try {
            ClassPathResource resource = new ClassPathResource(DEFAULT_DATA_JSON_PATH);
            String json = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            return new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
