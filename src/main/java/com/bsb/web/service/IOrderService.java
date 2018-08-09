package com.bsb.web.service;

import com.bsb.common.ServerResponse;

import java.util.Map;

/**
 * @author zeng
 */
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliPayCallBack(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
}
