package com.tools.commonservice.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import java.text.SimpleDateFormat;

public class JsonUtils {
    public static ObjectMapper objectMapper = null;

    static {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true) //美化json输出, 换行
                .serializationInclusion(JsonInclude.Include.USE_DEFAULTS)  //序列化策略
                .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE )  //下划线转驼峰
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));  //将时间戳转时间

        objectMapper = builder.build();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SimpleModule simpleModule = new SimpleModule();
        //long to String format
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);

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
