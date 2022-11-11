package com.lin.reggie.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.reggie.common.Result;
import com.lin.reggie.entity.Dish;
import com.lin.reggie.mapper.DishMapper;
import com.lin.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;

    @Override
    public Result<Page> getDishPageList(int page,int pageSize){
        Page pageInfo = new Page<Dish>(page,pageSize);
        dishMapper.selectPage(pageInfo,null);
        return Result.success(pageInfo);
    }
}
