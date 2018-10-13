package com.bsb.web.controller.backend;

import com.bsb.common.Const;
import com.bsb.common.ServerResponse;
import com.bsb.util.CookieUtil;
import com.bsb.util.JsonUtil;
import com.bsb.util.RedisUtil;
import com.bsb.web.pojo.User;
import com.bsb.web.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/login")
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse response) {

        ServerResponse<User> serverResponse = userService.login(username, password);
        if (serverResponse.isSuccess()) {
            User user = serverResponse.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
//                session.setAttribute(Const.CURRENT_USER, user);
                CookieUtil.writeLoginToken(response, session.getId());
                redisUtil.setRedisValueEx(session.getId(), JsonUtil.objToString(serverResponse.getData()), Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
                return serverResponse;
            } else {
                ServerResponse.createByErrorMsg("身份非管理员，登录失败");
            }
        }

        return serverResponse;
    }
}
