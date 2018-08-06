package com.bsb.web.controller.backend;

import com.bsb.common.Const;
import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.web.pojo.User;
import com.bsb.web.service.ICategoryService;
import com.bsb.web.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/addCategory")
    public ServerResponse addCategory(HttpSession session, String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") int parentId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse getChildrenParallelCategory(HttpSession session,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,
                                                             @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
