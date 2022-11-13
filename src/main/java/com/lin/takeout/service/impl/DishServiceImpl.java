package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.DishDto;
import com.lin.takeout.entity.Dish;
import com.lin.takeout.entity.DishFlavor;
import com.lin.takeout.mapper.DishFlavorMapper;
import com.lin.takeout.mapper.DishMapper;
import com.lin.takeout.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;

    @Override
    public Result<Page> getDishPageList(int page,int pageSize){
        Page pageInfo = new Page<Dish>(page,pageSize);
        dishMapper.selectPage(pageInfo,null);
        return Result.success(pageInfo);
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
}
