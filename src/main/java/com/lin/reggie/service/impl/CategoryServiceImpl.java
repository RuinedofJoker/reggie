package com.lin.reggie.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.reggie.common.Result;
import com.lin.reggie.entity.Category;
import com.lin.reggie.mapper.CategoryMapper;
import com.lin.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;


    @Override
    public Result<Page> getPage(int page, int pageSize) {
        Page pageInfo = new Page(page,pageSize);
        categoryMapper.selectPage(pageInfo,null);

        return Result.success(pageInfo);
    }

    @Override
    public Result deleteCategoryById(long id) {
        if (categoryMapper.deleteById(id) != 0){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public Result setCategory(Category category) {
        if (categoryMapper.selectBySort(category.getSort()) != null){
            return Result.error("添加失败，排序重复");
        }
        if (categoryMapper.insert(category) != 0){
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result changeCategory(Category category) {
        if (categoryMapper.selectBySort(category.getSort()) != null){
            return Result.error("修改失败，排序重复");
        }
        if (categoryMapper.updateById(category) != 0){
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
}
