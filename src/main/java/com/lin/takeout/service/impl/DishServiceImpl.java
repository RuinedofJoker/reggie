package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.DishDto;
import com.lin.takeout.entity.Category;
import com.lin.takeout.entity.Dish;
import com.lin.takeout.entity.DishFlavor;
import com.lin.takeout.mapper.CategoryMapper;
import com.lin.takeout.mapper.DishFlavorMapper;
import com.lin.takeout.mapper.DishMapper;
import com.lin.takeout.service.CommonService;
import com.lin.takeout.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    CommonService commonService;

    //按id查询菜品
    @Override
    public Result<DishDto> getDishById(long id) {

        Dish dish = null;
        ArrayList<DishFlavor> list = null;

        dish = dishMapper.selectById(id);
        list = (ArrayList<DishFlavor>) dishFlavorMapper.selectByDishId(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);

        return Result.success(dishDto);
    }

    //修改菜品是否还有的状态(可批量删除)
    @Transactional
    @Override
    public Result<String> updateDishStatus(int status, String id) {

        String[] ids = id.split(",");

        for (int i = 0;i < ids.length;i++){
            if (dishMapper.updateStatusById(status,Long.parseLong(ids[i])) == -1)
                return Result.error("修改状态失败");
        }

        return Result.success("修改成功");
    }

    //修改菜品
    @Transactional
    @Override
    public Result<String> updateDish(DishDto dishDto) throws Exception {

        Dish dish = dishDto;
        Dish nowDish = dishMapper.selectById(dish.getId());

        //获取之前菜品的图片
        String deletedImg = nowDish.getImage();

        if (dishFlavorMapper.deleteByDishId(dish.getId()) == -1)
            return Result.error("修改菜品偏好失败");

        ArrayList<DishFlavor> list = (ArrayList) dishDto.getFlavors();

        for (int i = 0;i < list.size();i++){

            DishFlavor dishFlavor = list.get(i);

            if (dishFlavorMapper.insert(dishFlavor) == -1)
                return Result.error("修改菜品偏好失败");

        }

        //删除之前菜品的图片（如果没有上传新图片则不会删除）
        if (!deletedImg.equals(nowDish.getImage()))
            commonService.deleteImg(deletedImg);

        return Result.success("修改成功");
    }

    //根据菜品分类查询该分类下所有菜品
    @Override
    public Result<List<DishDto>> getDishByCategoryId(long categoryId) {

        //查出该分类下所有菜品的id
        List<Dish> list = dishMapper.selectByCategoryId(categoryId);

        //按id查询所有菜品
        List<DishDto> dishDtoList = list.stream().map((item) -> {

            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            dishDto.setFlavors(dishFlavorMapper.selectByDishId(dishDto.getId()));

            Category category = new Category();
            category.setId(dishDto.getCategoryId());
            category = categoryMapper.selectById(category);

            if (category != null)
                dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());

        return Result.success(dishDtoList);
    }

    //菜品的分页查询
    @Override
    public Result<Page> getDishPageList(int page,int pageSize, String name){

        Page<Dish> dishPage = new Page<Dish>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<DishDto>(page,pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishMapper.selectPage(dishPage,queryWrapper);
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        List<Dish> records = dishPage.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            //根据菜品分类id查询菜品
            Long categoryId = item.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return Result.success(dishDtoPage);
    }

    //添加菜品
    @Override
    @Transactional
    public Result<String> saveDish(DishDto dishDto) {

        Dish dish = dishDto;
        Dish dishName = dishMapper.selectByName(dish.getName());
        if (dishName != null){
            return Result.error("菜名重复");
        }

        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        if (dishMapper.insert(dish) != 1){
            return Result.error("添加菜品失败");
        }
        dish = dishMapper.selectByName(dish.getName());
        ArrayList list = (ArrayList) dishDto.getFlavors();
        int size = list.size();

        for (int i = 0;i < size;i++){

            DishFlavor dishFlavor = (DishFlavor) list.get(i);
            dishFlavor.setDishId(dish.getId());

            if (dishFlavorMapper.insert(dishFlavor) != 1)
                return Result.error("添加第"+i+"个偏好失败");

        }

        return Result.success("");
    }

    //按菜品id删除菜品
    @Transactional
    @Override
    public Result<String> deleteDishById(String id) throws Exception {

        String[] ids = id.split(",");
        String[] imgs = new String[ids.length];

        //查询要删除的菜品的图片
        for (int i = 0;i < ids.length;i++){

            imgs[i] = dishMapper.selectById(Long.parseLong(ids[i])).getImage();

            if (dishMapper.deleteById(Long.parseLong(ids[i])) != -1 && dishFlavorMapper.deleteByDishId(Long.parseLong(ids[i])) != -1){

                //删除该菜品的图片
                commonService.deleteImg(imgs[i]);
                return Result.success("删除成功");
            }
        }

        return Result.error("删除失败");
    }
}
