package com.bsb.web.controller.backend;

import com.bsb.common.Const;
import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.util.CookieUtil;
import com.bsb.util.JsonUtil;
import com.bsb.util.RedisUtilFactory;
import com.bsb.web.pojo.User;
import com.bsb.web.service.IOrderService;
import com.bsb.web.service.IUserService;
import com.bsb.web.vo.OrderVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/manage/order")
public class OrderManageController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisUtilFactory redisUtilFactory;

    @GetMapping("/list")
    public ServerResponse<PageInfo> orderList(HttpServletRequest request,
                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {
            //管理员权限
            return orderService.manageList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

    @GetMapping("/getDetail")
    public ServerResponse<OrderVo> getDetail(HttpServletRequest request, Long orderNo) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {
            //管理员权限
            return orderService.manageDetail(orderNo);
        } else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

    @PostMapping("/search")
    public ServerResponse<PageInfo> search(HttpServletRequest request, Long orderNo,
                                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {
            //管理员权限
            return orderService.manageSearch(orderNo, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

    @GetMapping("/sendGoods")
    public ServerResponse<String> sendGoods(HttpServletRequest request, Long orderNo) {
        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {
            //管理员权限
            return orderService.manageSendGoods(orderNo);
        } else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }
}
