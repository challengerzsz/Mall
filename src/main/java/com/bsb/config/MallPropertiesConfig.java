package com.bsb.config;

import com.bsb.properties.MallProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zeng
 */
@Configuration
@EnableConfigurationProperties(MallProperties.class)
public class MallPropertiesConfig {
}
