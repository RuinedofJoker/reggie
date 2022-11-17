package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Category;
import com.lin.takeout.entity.Dish;
import com.lin.takeout.entity.Setmeal;
import com.lin.takeout.mapper.CategoryMapper;
import com.lin.takeout.mapper.DishMapper;
import com.lin.takeout.mapper.SetmealMapper;
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
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;

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

            List<Dish> dishList = dishMapper.selectByCategoryId(id);

            //删除菜品(如果该类别内有菜品)
            if (dishList.size() != 0){

                String dishId = "";
                //将dishId拼接起来
                for (Dish dish:dishList){
                    dishId = dish.getId()+",";
                }
                //去掉最后一个,
                dishId = dishId.substring(0,dishId.length()-1) + dishId.substring(dishId.length());

                dishService.deleteDishById(dishId);
            }
        }else if (category.getType() == 1){

            List<Setmeal> setmealList = setmealMapper.selectList(new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getCategoryId,id));

            //删除套餐(如果该类别内有套餐)
            if (setmealList.size() != 0){

                String setmealId = "";
                //将setmealId拼接起来
                for (Setmeal setmeal:setmealList){
                    setmealId = setmeal.getId()+",";
                }
                //去掉最后一个,
                setmealId = setmealId.substring(0,setmealId.length()-1) + setmealId.substring(setmealId.length());

                setmealService.deleteSetmealById(setmealId);
            }
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
    @Transactional
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
    @Transactional
    public Result updateCategory(Category category) {

        if (categoryMapper.selectBySort(category.getSort()) != null)
            return Result.error("修改失败，排序重复");

        if (categoryMapper.updateById(category) != 0)
            return Result.success("修改成功");

        return Result.error("修改失败");
    }
}
