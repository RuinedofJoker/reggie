package com.lin.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Category;

import java.util.List;

public interface CategoryService {
    Result<Page> getCategoryPage(int page, int pageSize);

    Result saveCategory(Category category);

    Result updateCategory(Category category);

    Result removeCategoryById(long id) throws Exception;

    Result<List<Category>> getCategoryByType(int type);

    Result<List<Category>> getCategoryList();
}
