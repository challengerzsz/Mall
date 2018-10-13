package com.bsb.config;

import com.bsb.web.controller.common.interceptor.AuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author zeng on 18-10-13.
 * @version 1.0
 */
@Configuration
public class AppConfig extends WebMvcConfigurationSupport {

    @Autowired
    private AuthorityInterceptor authorityInterceptor;

    /**
     * 添加权限验证拦截器
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorityInterceptor).addPathPatterns("/manage/**").excludePathPatterns("/manage/user/login");
    }
}
