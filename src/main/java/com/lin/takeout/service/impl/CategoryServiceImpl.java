package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Category;
import com.lin.takeout.mapper.CategoryMapper;
import com.lin.takeout.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;


    @Override
    public Result<Page> getPage(int page, int pageSize) {
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryMapper.selectPage(pageInfo,queryWrapper);
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
    public Result<List<Category>> getCategoryByType(int type) {
        List<Category> list = categoryMapper.selectByType(type);
        if (list != null){
            return Result.success(list);
        }
        return Result.error("未查询到类别为"+type+"的菜品");
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
