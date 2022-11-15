package com.lin.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.SetmealDto;
import com.lin.takeout.entity.Setmeal;

import java.util.List;

public interface SetmealService {

    Result<Page> getSetmealPageList(int page, int pageSize,String name);

    Result<String> setSetmeal(SetmealDto setmealDto,long userId);

    Result<SetmealDto> getSetmealById(long id);

    Result<String> changeSetmealById(SetmealDto setmealDto,long userId) throws Exception;

    Result<String> deleteSetmealById(long id) throws Exception;

    Result<String> changeSetmealStatus(int status,String id, long userId);

    Result<List<Setmeal>> getCategoryList(Setmeal setmeal);
}
