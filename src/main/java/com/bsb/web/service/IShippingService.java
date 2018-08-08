package com.bsb.web.service;

import com.bsb.common.ServerResponse;
import com.bsb.web.pojo.Shipping;
import com.github.pagehelper.PageInfo;

/**
 * @author zeng
 */
public interface IShippingService {

    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse<String> delete(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(int pageNum, int pageSize, Integer userId);
}
