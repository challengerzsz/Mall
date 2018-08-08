package com.bsb.web.controller.portal;

import com.bsb.common.Const;
import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.web.pojo.Shipping;
import com.bsb.web.pojo.User;
import com.bsb.web.service.IShippingService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private IShippingService shippingService;

    @PostMapping("/add")
    public ServerResponse add(HttpSession session, Shipping shipping) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return shippingService.add(user.getId(), shipping);
    }

    @PostMapping("/delete")
    public ServerResponse delete(HttpSession session, Integer shippingId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return shippingService.delete(user.getId(), shippingId);
    }

    @PostMapping("/update")
    public ServerResponse update(HttpSession session, Shipping shipping) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return shippingService.update(user.getId(), shipping);
    }

    @PostMapping("/search")
    public ServerResponse<Shipping> search(HttpSession session, Integer shippingId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return shippingService.select(user.getId(), shippingId);
    }

    @GetMapping("/list")
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpSession session) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return shippingService.list(pageNum, pageSize, user.getId());
    }
}
