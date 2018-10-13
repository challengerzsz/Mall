package com.bsb.web.controller.backend;

import com.bsb.common.ServerResponse;
import com.bsb.properties.MallProperties;
import com.bsb.util.RedisUtil;
import com.bsb.web.pojo.Product;
import com.bsb.web.service.IFileService;
import com.bsb.web.service.IProductService;
import com.bsb.web.service.IUserService;
import com.bsb.web.vo.ProductDetiailVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IProductService productService;
    @Autowired
    private IFileService fileService;
    @Autowired
    private MallProperties mallProperties;

    @PostMapping("/save")
    public ServerResponse saveProduct(Product product) {

        return productService.saveOrUpdateProduct(product);
    }

    @PostMapping("/updateProductStatus")
    public ServerResponse updateProductStatus(Integer productId, Integer status) {

        //修改产品上架状态
        return productService.updateProductStatus(productId, status);

    }

    @PostMapping("/getDetail")
    public ServerResponse<ProductDetiailVo> getDetail(Integer productId) {

        return productService.manageProductDetail(productId);
    }

    @PostMapping("/getList")
    public ServerResponse getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return productService.getProductList(pageNum, pageSize);
    }

    @PostMapping("/search")
    public ServerResponse searchProduct(String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return productService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @PostMapping("/upload")
    public ServerResponse upload(@RequestParam(value = "file", required = false) MultipartFile file,
                                 HttpServletRequest request) {

        String path = request.getSession().getServletContext().getRealPath("upload");
        logger.info("request取出的路径 {}", path);

        String targetFileName = fileService.upload(file, path);
        String url = mallProperties.getFtp().getServerHttpPrefix() + targetFileName;

        Map fileMap = new HashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);

    }

    @PostMapping("/richTextUpload")
    public Map richTextImgUpload(@RequestParam(value = "file", required = false) MultipartFile file,
                                 HttpServletRequest request, HttpServletResponse response) {

        Map resultMap = new HashMap();

        //富文本中做自己要求simditor

        String path = request.getSession().getServletContext().getRealPath("upload");

        String targetFileName = fileService.upload(file, path);

        if (StringUtils.isBlank(targetFileName)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }

        String url = mallProperties.getFtp().getServerHttpPrefix() + targetFileName;

        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);

        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }
}
