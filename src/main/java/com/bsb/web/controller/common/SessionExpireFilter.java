package com.bsb.web.controller.common;

import com.bsb.common.Const;
import com.bsb.util.CookieUtil;
import com.bsb.util.JsonUtil;
import com.bsb.util.RedisUtilFactory;
import com.bsb.web.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "SessionExpireFilter", urlPatterns = "*")
public class SessionExpireFilter implements Filter {

    @Autowired
    private RedisUtilFactory redisUtilFactory;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJson = redisUtilFactory.getRedisValue(loginToken);
            User user = JsonUtil.stringToObj(userJson, User.class);
            if (user != null) {
                //如果user不为空　重置session时间
                redisUtilFactory.expire(loginToken, Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
