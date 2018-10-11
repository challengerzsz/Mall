package com.bsb.web.controller.portal;

import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.util.CookieUtil;
import com.bsb.util.JsonUtil;
import com.bsb.util.RedisUtil;
import com.bsb.web.pojo.Shipping;
import com.bsb.web.pojo.User;
import com.bsb.web.service.IShippingService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IShippingService shippingService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/add")
    public ServerResponse add(HttpServletRequest request, Shipping shipping) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtil.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return shippingService.add(user.getId(), shipping);
    }

    @PostMapping("/delete")
    public ServerResponse delete(HttpServletRequest request, Integer shippingId) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtil.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return shippingService.delete(user.getId(), shippingId);
    }

    @PostMapping("/update")
    public ServerResponse update(HttpServletRequest request, Shipping shipping) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtil.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return shippingService.update(user.getId(), shipping);
    }

    @PostMapping("/search")
    public ServerResponse<Shipping> search(HttpServletRequest request, Integer shippingId) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtil.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return shippingService.select(user.getId(), shippingId);
    }

    @GetMapping("/list")
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpServletRequest request) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtil.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return shippingService.list(pageNum, pageSize, user.getId());
    }
}
