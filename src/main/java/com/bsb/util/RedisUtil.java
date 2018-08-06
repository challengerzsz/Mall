package com.bsb.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zeng
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    public void setRedisValue(String key, String value) {

        redisTemplate.opsForValue().set(key ,value, 30, TimeUnit.MINUTES);

    }

    public String getRedisValue(String key) {

        return (String) redisTemplate.opsForValue().get(key);
    }
}
