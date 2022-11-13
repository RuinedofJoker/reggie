package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Dish;
import com.lin.takeout.mapper.SetmealMapper;
import com.lin.takeout.service.SetmealService;
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
