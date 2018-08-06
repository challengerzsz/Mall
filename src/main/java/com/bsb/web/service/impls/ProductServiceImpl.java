package com.bsb.web.service.impls;

import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.web.dao.IProductMapper;
import com.bsb.web.pojo.Product;
import com.bsb.web.service.IProductService;
import com.bsb.web.vo.ProductDetiailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zeng
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductMapper productMapper;

    public ServerResponse saveOrUpdateProduct(Product product) {

        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null) {
                int resultCount = productMapper.updateByPrimaryKey(product);

                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMsg("更新产品成功");
                }
                return ServerResponse.createByErrorMsg("更新产品失败");
            } else {

                int resultCount = productMapper.insert(product);
                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMsg("新增产品成功");
                }
                return ServerResponse.createByErrorMsg("新增产品失败");
            }
        }

        return ServerResponse.createByErrorMsg("新增或更新产品参数错误");
    }

    public ServerResponse<String> updateProductStatus(Integer productId, Integer status) {

        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMsg("修改产品销售状态成功");
        }

        return ServerResponse.createByErrorMsg("修改产品销售状态失败");
    }


    public ServerResponse<Object> manageProductDetail(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMsg("产品已下架或被删除");
        }

//        ProductDetiailVo productDetiailVo =

        return null;
    }

    private void assembleProductDetailVo(Product product) {

        ProductDetiailVo productDetiailVo = new ProductDetiailVo();

        productDetiailVo.setId(product.getId());
        productDetiailVo.setPrice(product.getPrice());
        productDetiailVo.setMainImage(product.getMainImage());
        productDetiailVo.setSubImage(product.getMainImage());
        productDetiailVo.setCategoryId(product.getCategoryId());
        productDetiailVo.setDetail(product.getDetail());
        productDetiailVo.setName(product.getName());
        productDetiailVo.setSubtitle(product.getSubtitle());
        productDetiailVo.setStatus(product.getStatus());
        productDetiailVo.setStock(product.getStock());

    }
}
