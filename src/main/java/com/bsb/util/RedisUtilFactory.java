package com.bsb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zeng
 */
@Component
public class RedisUtilFactory {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * redis存储方法
     * @param key
     * @param value
     */
    public void setRedisValue(String key, String value) {

        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            logger.error("set key {} value {} error", key, value, e);
        }

    }

    /**
     * 设置键值对的时候设置失效时间
     * @param key
     * @param value
     * @param exTime
     */
    public void setRedisValueEx(String key, String value, int exTime) {

        try {
            logger.debug("enter set method");
            redisTemplate.opsForValue().set(key, value, exTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("setEx key {} value {} error", key, value, e);
        }

    }

    /**
     * 设置失效时间
     * @param key
     * @param exTime
     */
    public Boolean expire(String key, int exTime) {

        Boolean result = false;
        try {
            result = redisTemplate.expire(key, exTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("expire key {} error", key, e);
        }

        return result;
    }

    public String getRedisValue(String key) {

        String result = null;
        try {
            result =  (String) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("get key {} error", key, e);
        }

        return result;
    }

    /**
     * 删除键值
     * @param key
     * @return
     */
    public Boolean delete(String key) {

        Boolean result = false;

        try {
            result = redisTemplate.delete(key);
        } catch (Exception e) {
            logger.error("delete key {} error", key, e);
        }

        return result;
    }

    // TODO: 2018/10/5 6-6


}
