package com.bsb.web.controller.backend;

import com.bsb.common.ServerResponse;
import com.bsb.web.service.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/addCategory")
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return categoryService.addCategory(categoryName, parentId);

    }

    @PostMapping("/updateCategoryName")
    public ServerResponse setCategoryName(Integer categoryId, String categoryName) {

        return categoryService.updateCategoryName(categoryId, categoryName);

    }

    @GetMapping("/getCategory")
    public ServerResponse getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        //查询子节点category信息 不递归
        return categoryService.getChildrenParallelCategory(categoryId);
    }

    @GetMapping("/getCategoryAndDeepChildren")
    public ServerResponse getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        //查询当前节点id和递归子节点的id
        return categoryService.selectCategoryAndChildrenById(categoryId);

    }

}
