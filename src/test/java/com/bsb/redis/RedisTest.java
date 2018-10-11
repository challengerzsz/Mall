package com.bsb.redis;

import com.bsb.config.RedisConfig;
import com.bsb.properties.ClusterConfigurationProperties;
import com.bsb.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private ClusterConfigurationProperties clusterConfigurationProperties;

    @Test
    public void set() {
        redisUtil.setRedisValueEx("123", "1212", 30);
    }

    @Test
    public void cluster() {

//        byte[] bytes;
//        RedisClusterConnection clusterConnection = redisConnectionFactory.getClusterConnection();
//        for (int i = 0; i < 10; i++) {
//            bytes = new String(i + "").getBytes();
//            clusterConnection.set(bytes, bytes);
//        }
//
//        List<String> clusters = clusterConfigurationProperties.getNodes();
//        System.out.println(clusters.size());

        for (int i = 0; i < 100; i++) {
            redisUtil.setRedisValue(i + "", i + "  ");
        }
    }
}
