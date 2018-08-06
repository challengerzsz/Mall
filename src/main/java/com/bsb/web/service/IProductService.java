package com.bsb.web.service;

import com.bsb.common.ServerResponse;
import com.bsb.web.pojo.Product;

/**
 * @author zeng
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> updateProductStatus(Integer productId, Integer status);
}
