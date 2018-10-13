package com.bsb.web.controller.common.interceptor;

import com.bsb.common.Const;
import com.bsb.common.ServerResponse;
import com.bsb.util.CookieUtil;
import com.bsb.util.JsonUtil;
import com.bsb.util.RedisUtil;
import com.bsb.web.pojo.User;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author zeng on 18-10-13.
 * @version 1.0
 */
@Component
public class AuthorityInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJson = redisUtil.getRedisValue(loginToken);
            user = JsonUtil.stringToObj(userJson, User.class);
        }

        if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)) {
            //重置响应
            response.reset();
            //防止乱码
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter printWriter = response.getWriter();

            if (user == null) {

                //拦截器定制富文本上传逻辑(富文本控件指定了返回类型)
                if (StringUtils.equals(request.getRequestURI(), "/manage/product/richTextUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "未登录，请以管理员身份登录");
                    printWriter.print(JsonUtil.objToString(resultMap));
                } else {
                    printWriter.print(JsonUtil.objToString(ServerResponse.createByErrorMsg("用户未登录")));
                }
            } else {
                //拦截器定制富文本上传逻辑
                if (StringUtils.equals(request.getRequestURI(), "/manage/product/richTextUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "需管理员权限");
                    printWriter.print(JsonUtil.objToString(resultMap));
                } else {
                    printWriter.print(JsonUtil.objToString(ServerResponse.createByErrorMsg("非管理员权限，无权操作")));
                }
            }
            printWriter.flush();
            printWriter.close();

            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("post");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("after");
    }
}
