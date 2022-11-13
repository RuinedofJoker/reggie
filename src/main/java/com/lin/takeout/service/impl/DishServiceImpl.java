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

    @Override
    public Result<String> changeDishStatus(int status, long id) {
        if (dishMapper.updateStatusById(status,id) == -1){
            return Result.error("修改状态失败");
        }
        return Result.success("修改成功");
    }

    @Override
    public Result<String> changeDish(DishDto dishDto, long userId) throws Exception {
        Dish dish = dishDto;
        Dish nowDish = dishMapper.selectById(dish.getId());
        dish.setUpdateTime(LocalDateTime.now());
        dish.setUpdateUser(userId);
        if (nowDish == null){
            return Result.error("未查询到菜品");
        }
        String deletedImg = nowDish.getImage();
        if (dishFlavorMapper.deleteByDishId(dish.getId()) == -1){
            return Result.error("修改菜品偏好失败");
        }
        ArrayList list = (ArrayList) dishDto.getFlavors();
        for (int i = 0;i < list.size();i++){
            DishFlavor dishFlavor = (DishFlavor) list.get(i);
            dishFlavor.setUpdateTime(LocalDateTime.now());
            dishFlavor.setUpdateUser(userId);
            if (dishFlavorMapper.insert(dishFlavor) == -1){
                return Result.error("修改菜品偏好失败");
            }
        }
        if (dishMapper.updateById(dish) == -1){
            return Result.error("修改菜品失败");
        }
        if (!deletedImg.equals(nowDish.getImage())){
            commonService.deleteImg(deletedImg);
        }
        return Result.success("修改成功");
    }

    @Override
    public Result<List<Dish>> getDishByCategoryId(long categoryId) {
        List<Dish> list = dishMapper.selectByCategoryId(categoryId);
        return Result.success(list);
    }

    @Override
    public Result<Page> getDishPageList(int page,int pageSize, String name){
        Page<Dish> dishPage = new Page<Dish>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<DishDto>(page,pageSize);

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishMapper.selectPage(dishPage,queryWrapper);
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        List<Dish> records = dishPage.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
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

    @Override
    public Result<String> addDish(DishDto dishDto,long userId) {
        Dish dish = dishDto;
        Dish dishName = dishMapper.selectByName(dish.getName());
        if (dishName != null){
            return Result.error("菜名重复");
        }
        dish.setCreateUser(userId);
        dish.setUpdateUser(userId);
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
            dishFlavor.setCreateUser(userId);
            dishFlavor.setUpdateUser(userId);
            dishFlavor.setCreateTime(LocalDateTime.now());
            dishFlavor.setUpdateTime(LocalDateTime.now());
            dishFlavor.setDishId(dish.getId());
            if (dishFlavorMapper.insert(dishFlavor) != 1){
                return Result.error("添加第"+i+"个偏好失败");
            }
        }

        return Result.success("");
    }

    @Override
    public Result<String> deleteDishById(long id) throws Exception {
        String img = dishMapper.selectById(id).getImage();
        if (dishMapper.deleteById(id) != -1 && dishFlavorMapper.deleteByDishId(id) != -1){
            commonService.deleteImg(img);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
