package com.bsb.web.service;

import com.bsb.common.ServerResponse;
import com.bsb.web.pojo.Category;

import java.util.List;

/**
 * @author zeng
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse selectCategoryAndChildrenById(Integer categoryId);
}
