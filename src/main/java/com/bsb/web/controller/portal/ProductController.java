package com.bsb.web.controller.portal;

import com.bsb.common.ServerResponse;
import com.bsb.web.service.IProductService;
import com.bsb.web.vo.ProductDetiailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/getDetail")
    public ServerResponse<ProductDetiailVo> getDetail(Integer productId) {

        return productService.getProductDetail(productId);
    }

    @GetMapping("/listProduct")
    public ServerResponse<PageInfo> getList(@RequestParam(value = "keyWord", required = false) String keyWord,
                                            @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {

        return productService.getProductByKeyWordCategory(keyWord, categoryId, pageNum, pageSize, orderBy);
    }
}
