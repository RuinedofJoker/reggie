package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Category;
import com.lin.takeout.mapper.CategoryMapper;
import com.lin.takeout.service.CategoryService;
import com.lin.takeout.service.DishService;
import com.lin.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    //分页查询菜品类别
    @Override
    public Result<Page> getCategoryPage(int page, int pageSize) {

        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryMapper.selectPage(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }

    //查询所有菜品类别
    @Override
    public Result<List<Category>> getCategoryList() {
        return Result.success(categoryMapper.selectAll());
    }

    //删除菜品类别
    @Transactional
    @Override
    public Result removeCategoryById(long id) throws Exception{

        Category category = categoryMapper.selectById(id);

        if (category.getType() == 1){

            //删除菜品
            dishService.deleteDishById(id+"");

        }else if (category.getType() == 1){

            //删除套餐
            setmealService.deleteSetmealById(id+"");

        }

        if (categoryMapper.deleteById(id) != 0){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    //查询菜品/套餐类别（type=1为菜品，type=2为套餐）
    @Override
    public Result<List<Category>> getCategoryByType(int type) {

        List<Category> list = categoryMapper.selectByType(type);
        if (list != null){
            return Result.success(list);
        }
        return Result.error("未查询到类别为"+type+"的菜品");
    }

    //添加菜品分类
    @Override
    public Result saveCategory(Category category) {

        if (categoryMapper.selectBySort(category.getSort()) != null)
            return Result.error("添加失败，排序重复");

        if (categoryMapper.insert(category) != 0){
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    ///修改菜品分类
    @Override
    public Result updateCategory(Category category) {

        if (categoryMapper.selectBySort(category.getSort()) != null)
            return Result.error("修改失败，排序重复");

        if (categoryMapper.updateById(category) != 0)
            return Result.success("修改成功");

        return Result.error("修改失败");
    }
}
