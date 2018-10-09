package com.bsb.web.controller.backend;

import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.util.CookieUtil;
import com.bsb.util.JsonUtil;
import com.bsb.util.RedisUtil;
import com.bsb.web.pojo.User;
import com.bsb.web.service.ICategoryService;
import com.bsb.web.service.IUserService;
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
@RequestMapping("/manage/category")
public class CategoryManageController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/addCategory")
    public ServerResponse addCategory(HttpServletRequest request, String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") int parentId) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtil.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {
            //管理员权限

            return categoryService.addCategory(categoryName, parentId);

        } else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

    @PostMapping("/updateCategoryName")
    public ServerResponse setCategoryName(HttpServletRequest request, Integer categoryId, String categoryName) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtil.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {
            //管理员权限
            return categoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

    @GetMapping("/getCategory")
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtil.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {
            //查询子节点category信息 不递归

            return categoryService.getChildrenParallelCategory(categoryId);

        } else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

    @GetMapping("/getCategoryAndDeepChildren")
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest request,
                                                             @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        String loginToken = CookieUtil.readLoginToken(request);
        logger.error("error {}", loginToken);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        String userJson = redisUtil.getRedisValue(loginToken);
        User user = JsonUtil.stringToObj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {
            //查询当前节点id和递归子节点的id
            return categoryService.selectCategoryAndChildrenById(categoryId);

        } else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

}
