package com.tools.commonservice.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@AutoConfigureAfter({RedisAutoConfiguration.class})
public class RedisAutoConfig {
    public RedisAutoConfig() {
    }

    @Bean
    public RedisService redisService(RedisTemplate<String, String> redisTemplate, Environment env) {
        String appName = env.getProperty("info.name");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return new RedisService(redisTemplate, appName);
    }

}

