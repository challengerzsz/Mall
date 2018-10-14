package com.bsb.web.service.impls;

import com.bsb.common.Const;
import com.bsb.common.ResponseCode;
import com.bsb.common.ServerResponse;
import com.bsb.properties.MallProperties;
import com.bsb.web.dao.ICategoryMapper;
import com.bsb.web.dao.IProductMapper;
import com.bsb.web.pojo.Category;
import com.bsb.web.pojo.Product;
import com.bsb.web.service.ICategoryService;
import com.bsb.web.service.IProductService;
import com.bsb.web.vo.ProductDetiailVo;
import com.bsb.web.vo.ProductListVo;
import com.github.pagehelper.Page;
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
    @Autowired
    private ICategoryService categoryService;

    public ServerResponse saveOrUpdateProduct(Product product) {

        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null) {
                int resultCount = productMapper.updateByPrimaryKeySelective(product);

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
        productListVo.setImageHost(mallProperties.getFtp().getServerHttpPrefix());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubTitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());

        return productListVo;
    }

    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            //完成sql模糊查询拼接产品名
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


    public ServerResponse<ProductDetiailVo> getProductDetail(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMsg("产品已下架或被删除");
        }

        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMsg("产品已下架或被删除");
        }

        ProductDetiailVo productDetiailVo = assembleProductDetailVo(product);

        return ServerResponse.createBySuccess(productDetiailVo);
    }

    public ServerResponse<PageInfo> getProductByKeyWordCategory(String keyWord, Integer categoryId, int pageNum, int pageSize, String orderBy) {

        if (StringUtils.isBlank(keyWord) && categoryId == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = new ArrayList<>();

        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && keyWord == null) {
                //无分类，且无关键字，返回空集
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = new ArrayList<>();
                PageInfo pageInfo = new PageInfo(productListVoList);

                return ServerResponse.createBySuccess(pageInfo);
            }

            categoryIdList = categoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }

        if (StringUtils.isNotBlank(keyWord)) {
            keyWord = new StringBuffer().append("%").append(keyWord).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);
        //处理排序
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                //orderBy方法排序格式
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }

        List<Product> productList = productMapper.selectByNameAndCategoryIds(
                StringUtils.isBlank(keyWord) ? null : keyWord, categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVo> productListVoList = new ArrayList<>();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }
}
