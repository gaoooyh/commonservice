package com.tools.commonservice.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;

public class JsonUtils {
    public static ObjectMapper objectMapper = null;

    static {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true)
                .serializationInclusion(JsonInclude.Include.USE_DEFAULTS)
                .propertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        objectMapper = builder.build();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String write(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception ex) {
            return "{}";
        }
    }

    public static <T> T read(String value, Class<T> tClass) {
        try {
            return objectMapper.readValue(value, tClass);
        } catch (Exception ex) {
            return null;
        }
    }

}
