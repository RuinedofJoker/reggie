package com.lin.reggie.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.reggie.common.Result;
import com.lin.reggie.entity.Dish;
import com.lin.reggie.mapper.SetmealMapper;
import com.lin.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;

    @Override
    public Result<Page> getSetmealPageList(int page, int pageSize) {
        Page pageInfo = new Page<Dish>(page,pageSize);
        setmealMapper.selectPage(pageInfo,null);
        return Result.success(pageInfo);
    }
}
