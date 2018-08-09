package com.bsb.web.controller.backend;

import com.bsb.common.Const;
import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.web.pojo.User;
import com.bsb.web.service.IOrderService;
import com.bsb.web.service.IUserService;
import com.bsb.web.vo.OrderVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/manage/order")
public class OrderManageController {


    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;

    @GetMapping("/list")
    public ServerResponse<PageInfo> orderList(HttpSession session,
                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse<OrderVo> getDetail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse<PageInfo> search(HttpSession session, Long orderNo,
                                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse<String> sendGoods(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
