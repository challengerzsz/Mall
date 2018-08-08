package com.bsb.web.service;

import com.bsb.common.ServerResponse;
import com.bsb.web.vo.CartVo;

/**
 * @author zeng
 */
public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteProductFromCart(Integer userId, String productIds);

    ServerResponse<CartVo> getCartList(Integer userId);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}
