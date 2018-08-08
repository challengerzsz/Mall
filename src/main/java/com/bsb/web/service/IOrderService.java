package com.bsb.web.service;

import com.bsb.common.ServerResponse;

/**
 * @author zeng
 */
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);
}
