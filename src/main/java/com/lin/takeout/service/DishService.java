package com.lin.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.DishDto;

import java.util.List;

public interface DishService {

    Result<Page> getDishPageList(int page, int pageSize, String name);

    Result<String> addDish(DishDto dishDto,long userId);

    Result<String> deleteDishById(long id) throws Exception;

    Result<DishDto> getDishById(long id);

    Result<String> changeDish(DishDto dishDto,long userId) throws Exception ;

    Result<String> changeDishStatus(int status,long id);

    Result<List<DishDto>> getDishByCategoryId(long categoryId);
}
