package com.lin.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.reggie.common.Result;
import com.lin.reggie.entity.Category;

import java.util.List;

public interface CategoryService {
    Result<Page> getPage(int page, int pageSize);

    Result setCategory(Category category);

    Result changeCategory(Category category);

    Result deleteCategoryById(long id);

    Result<List<Category>> getCategoryByType(int type);
}
