package com.bsb.common;

import com.bsb.properties.ClusterConfigurationProperties;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 *
 * @author zeng on 18-10-14.
 * @version 1.0
 */
@Component
public class RedissonManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Config config = new Config();

    private Redisson redisson = null;

    /**
     * 构造器执行完了之后执行这个init方法
     */
    @PostConstruct
    private void init() {

        try {
            this.config.useClusterServers()
                    .setScanInterval(2000)
                    .addNodeAddress("redis://127.0.0.1:6379", "redis://127.0.0.1:6380", "redis://127.0.0.1:6381",
                            "redis://127.0.0.1:6382", "redis://127.0.0.1:6383");
            this.redisson = (Redisson) Redisson.create(config);
            logger.info("初始化Redisson成功");
        } catch (Exception e) {
            logger.error("Redisson 初始化失败", e);
        }
    }

    public Redisson getRedisson() {
        return redisson;
    }

}
