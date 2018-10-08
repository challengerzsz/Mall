package com.bsb.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static Logger logger = LoggerFactory.getLogger(CookieUtil.class);

    /**
     * 保证domain是在一级域名下，子域名即可看到这个cookie
     */
    private final static String COOKIE_DOMAIN = ".mall.com";

    private final static String COOKIE_NAME = "mall_login_token";

    public static void writeLoginToken(HttpServletResponse response, String token) {

        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        //根目录能够获取该cookie
        cookie.setPath("/");
        //若不设置cookie的有效期 生命周期为浏览器的生命周期 在内存不会持久化到硬盘
        cookie.setMaxAge(60 * 60 * 24 * 365);
        logger.info("write cookieName :{}, cookieValue :{}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    /**
     * 获取属于mall服务器下的cookie 并且返回cookie的值即登录时的sessionId
     * @param request
     * @return
     */
    public static String readLoginToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                logger.info("read cookieName :{} cookieValue :{}", cookie.getName(), cookie.getValue());
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    logger.info("return cookieName :{} cookieValue :{}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 登出时通过设置cookie的过期时间为0从而删除cookie
     * @param request
     * @param response
     */
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    //删除cookie
                    cookie.setMaxAge(0);
                    logger.info("delete cookieName :{} cookieValue :{}", cookie.getName(), cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }
}
