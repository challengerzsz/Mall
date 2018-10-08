package com.bsb.redis;

import com.bsb.util.RedisUtilFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private RedisUtilFactory redisUtilFactory;

    @Test
    public void set() {
        redisUtilFactory.setRedisValueEx("123", "1212", 30);
    }
}
