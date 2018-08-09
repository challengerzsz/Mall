package com.bsb.web.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.bsb.common.Const;
import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.web.pojo.User;
import com.bsb.web.service.IOrderService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IOrderService orderService;


    @PostMapping("/create")
    public ServerResponse create(HttpSession session, Integer shippingId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return orderService.createOrder(user.getId(), shippingId);
    }

    @PostMapping("/cancel")
    public ServerResponse cancel(HttpSession session, Long orderNo) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return orderService.cancel(user.getId(), orderNo);
    }

    @PostMapping("/getOrderCartProducts")
    public ServerResponse getOrderCartProducts(HttpSession session) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return orderService.getOrderCartProducts(user.getId());
    }

    @PostMapping("/pay")
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        String path = request.getSession().getServletContext().getRealPath("upload");

        return orderService.pay(orderNo, user.getId(), path);
    }

    @PostMapping("aliPayCallBack")
    public Object  aliPayCallBack(HttpServletRequest request) {

        Map<String,String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext(); ) {
            String name = (String) iterator.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr="";
            for (int i = 0; i < values.length; i++) {

                //其实就相当于if判断i与数组的长度好决定拼接的规则
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);
        }
        logger.info("支付宝回调，sign：{}， trade_status:{}, 参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        //验证回调是否为支付宝的回调，过滤重复通知
        //自己封装params 由于sdk没有移除sign_type会导致验签失败
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMsg("非法请求，验证不通过，否则将采用安全手段！！！");
            }

        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常 ", e);
        }

        // TODO: 18-8-9 验证回调的订单数据是否为预下单数据
        ServerResponse serverResponse = orderService.aliPayCallBack(params);
        if (serverResponse.isSuccess()) {
            return Const.AlipayCallBack.RESPONSE_SUCCESS;
        }

        return Const.AlipayCallBack.RESPONSE_FAILED;

    }

    @PostMapping("/queryOrderPayStatus")
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse response = orderService.queryOrderPayStatus(user.getId(), orderNo);

        if (response.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }

    @GetMapping("/detail")
    public ServerResponse getDetail(HttpSession session, Long orderNo) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return orderService.getOrderDetail(user.getId(), orderNo);
    }

    @GetMapping("/list")
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return orderService.getList(user.getId(), pageNum, pageSize);
    }
}
