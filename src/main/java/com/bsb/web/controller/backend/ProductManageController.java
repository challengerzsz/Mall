package com.bsb.web.controller.backend;

import com.bsb.common.Const;
import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.web.pojo.Product;
import com.bsb.web.pojo.User;
import com.bsb.web.service.IProductService;
import com.bsb.web.service.IUserService;
import com.bsb.web.vo.ProductDetiailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IProductService productService;

    @PostMapping("/save")
    public ServerResponse saveProduct(HttpSession session, Product product) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {
            //增加产品
            return productService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMsg("无权限操作");
        }

    }

    @PostMapping("/updateProductStatus")
    public ServerResponse updateProductStatus(HttpSession session, Integer productId, Integer status) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {
            //修改产品上架状态
            return productService.updateProductStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMsg("无权限操作");
        }

    }

    @PostMapping("/getDetail")
    public ServerResponse<ProductDetiailVo> getDetail(HttpSession session, Integer productId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {

            return productService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    @PostMapping("/getList")
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {

            return productService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    @PostMapping("/search")
    public ServerResponse searchProduct(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登录");
        }

        if (userService.checkAdminRole(user).isSuccess()) {

            return productService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }
}
