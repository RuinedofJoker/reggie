package com.lin.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.SetmealDto;
import com.lin.takeout.entity.Setmeal;

import java.util.List;

public interface SetmealService {

    Result<Page> getSetmealPage(int page, int pageSize,String name);

    Result<String> saveSetmeal(SetmealDto setmealDto);

    Result<SetmealDto> getSetmealById(long id);

    Result<String> updateSetmealById(SetmealDto setmealDto) throws Exception;

    Result<String> deleteSetmealById(String id) throws Exception;

    Result<String> updateSetmealStatus(int status,String id);

    Result<List<Setmeal>> getCategoryList(Setmeal setmeal);
}
