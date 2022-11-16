package com.lin.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.DishDto;

import java.util.List;

public interface DishService {

    Result<Page> getDishPageList(int page, int pageSize, String name);

    Result<String> saveDish(DishDto dishDto);

    Result<String> deleteDishById(String id) throws Exception;

    Result<DishDto> getDishById(long id);

    Result<String> updateDish(DishDto dishDto) throws Exception ;

    Result<String> updateDishStatus(int status,String id);

    Result<List<DishDto>> getDishByCategoryId(long categoryId);
}
