package com.bsb.web.controller.portal;

import com.bsb.common.Const;
import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.util.CookieUtil;
import com.bsb.util.JsonUtil;
import com.bsb.util.RedisUtilFactory;
import com.bsb.web.pojo.User;
import com.bsb.web.service.ICartService;
import com.bsb.web.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ICartService cartService;
    @Autowired
    private RedisUtilFactory redisUtilFactory;

    @PostMapping("/add")
    public ServerResponse<CartVo> add(HttpServletRequest request, Integer count, Integer productId) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.add(user.getId(), productId, count);
    }

    @PostMapping("/update")
    public ServerResponse<CartVo> update(HttpServletRequest request, Integer count, Integer productId) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.update(user.getId(), productId, count);
    }

    @PostMapping("/delete")
    public ServerResponse<CartVo> update(HttpServletRequest request, String productIds) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.deleteProductFromCart(user.getId(), productIds);
    }

    @GetMapping("/list")
    public ServerResponse<CartVo> getCartList(HttpServletRequest request) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.getCartList(user.getId());
    }


    @PostMapping("/selectAll")
    public ServerResponse<CartVo> selectAll(HttpServletRequest request) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
    }

    @PostMapping("/unSelectAll")
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest request) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    @PostMapping("/selectOne")
    public ServerResponse<CartVo> selectOne(HttpServletRequest request, Integer productId) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
    }

    @PostMapping("/unSelectOne")
    public ServerResponse<CartVo> unSelectOne(HttpServletRequest request, Integer productId) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
    }

    @GetMapping("/getCartProductCount")
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtilFactory.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }

        return cartService.getCartProductCount(user.getId());
    }

}
