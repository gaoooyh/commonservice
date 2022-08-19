package com.tools.commonservice.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.commonservice.exception.ApiException;
import com.tools.commonservice.exception.ErrorCode;
import com.tools.commonservice.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import java.io.IOException;
import java.time.Duration;

@Slf4j
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    //info.name
    private final String servicePrefix;
    private final ObjectMapper objectMapper;



    public RedisService(RedisTemplate redisTemplate, String servicePrefix) {
        this.redisTemplate = redisTemplate;
        this.servicePrefix = servicePrefix;
        this.objectMapper = JsonUtils.objectMapper;
    }

    //todo format json string to object
    public <T> T get(String key, Class<T> tClass) {
        String finalKey = this.getKey(key);
        String value = this.redisTemplate.opsForValue().get(finalKey);
        if (value == null) {
            return null;
        } else {
            try {
                return objectMapper.readValue(value, tClass);
            } catch (IOException var6) {
                log.error("Error Object format : {}", value, var6);
            }
        }
        return null;
    }
    /*

    public boolean tryLock(String key) {
        key = "LOCK_" + key;
        String finalKey = this.getKey(key);
        return this.redisTemplate.opsForValue().setIfAbsent(finalKey, UUID.randomUUID().toString(), Duration.ofMinutes(5L));
    }

    public boolean tryLock(String key, int waitSecond) {
        key = "LOCK_" + key;
        String finalKey = this.getKey(key);
        long start = System.currentTimeMillis();

        do {
            boolean result = this.redisTemplate.opsForValue().setIfAbsent(finalKey, UUID.randomUUID().toString(), Duration.ofMinutes(5L));
            if (result) {
                return result;
            }

            try {
                Thread.sleep(50L);
            } catch (Exception var8) {
                return false;
            }
        } while(System.currentTimeMillis() - start <= (long)(waitSecond * 1000));

        return false;
    }

    public void release(String key) {
        key = "LOCK_" + key;
        String finalKey = this.getKey(key);
        this.redisTemplate.delete(finalKey);
    }
    */

    public String get(String key) {
        String finalKey = this.getKey(key);
        return this.redisTemplate.opsForValue().get(finalKey);
    }

    public void set(String key, String value, int expireSecond) {
        String finalKey = this.getKey(key);

        this.redisTemplate.opsForValue().set(finalKey, value, Duration.ofSeconds(expireSecond));
    }

    public void set(String key, Object obj, int expireSecond) {
        String finalKey = this.getKey(key);

        try {
            String value = this.objectMapper.writeValueAsString(obj);
            this.redisTemplate.opsForValue().set(finalKey, value, Duration.ofSeconds(expireSecond));
        } catch (IOException ex) {
            throw new ApiException(ErrorCode.paramError("redis读取数据失败"));
        }
    }

    public void set(String key, String value) {
        this.set(key, value, 300);
    }

    public String getKey(String key) {
        return this.servicePrefix + "_" + key;
    }
}

