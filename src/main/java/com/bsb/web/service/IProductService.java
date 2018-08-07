package com.bsb.web.service;

import com.bsb.common.ServerResponse;
import com.bsb.web.pojo.Product;
import com.bsb.web.vo.ProductDetiailVo;
import com.github.pagehelper.PageInfo;

/**
 * @author zeng
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> updateProductStatus(Integer productId, Integer status);

    ServerResponse<ProductDetiailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetiailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeyWordCategory(String keyWord, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
