package com.bsb.web.service.impls;

import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.properties.MallProperties;
import com.bsb.web.dao.ICategoryMapper;
import com.bsb.web.dao.IProductMapper;
import com.bsb.web.pojo.Category;
import com.bsb.web.pojo.Product;
import com.bsb.web.service.IProductService;
import com.bsb.web.vo.ProductDetiailVo;
import com.bsb.web.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zeng
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductMapper productMapper;
    @Autowired
    private ICategoryMapper categoryMapper;
    @Autowired
    private MallProperties mallProperties;

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


    public ServerResponse<ProductDetiailVo> manageProductDetail(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMsg("产品已下架或被删除");
        }

        ProductDetiailVo productDetiailVo = assembleProductDetailVo(product);

        return ServerResponse.createBySuccess(productDetiailVo);

    }

    private ProductDetiailVo assembleProductDetailVo(Product product) {

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

        // TODO: 18-8-7 配置自己的ftp服务器,默认值加载
        productDetiailVo.setImageHost(mallProperties.getFtp().getServerHttpPrefix());

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            //默认根节点
            productDetiailVo.setParentCategoryId(0);
        } else {
            productDetiailVo.setParentCategoryId(category.getParentId());
        }

        productDetiailVo.setCreateTime(String.valueOf(product.getCreateTime()));
        productDetiailVo.setUpdateTime(String.valueOf(product.getUpdateTime()));

        return productDetiailVo;
    }

    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVoList = new ArrayList<>();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);

        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product) {

        ProductListVo productListVo = new ProductListVo();

        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        // TODO: 18-8-7 配置自己的ftp服务器,默认值加载
        productListVo.setImageHost(mallProperties.getFtp().getServerHttpPrefix());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubTitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());

        return productListVo;
    }

    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }

        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);

        List<ProductListVo> productListVoList = new ArrayList<>();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);

        return ServerResponse.createBySuccess(pageResult);
    }
}
