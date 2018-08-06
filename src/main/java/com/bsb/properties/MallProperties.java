package com.bsb.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zeng
 */
@ConfigurationProperties(prefix = "bsb.mall")
public class MallProperties {

    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
