package com.lin.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.reggie.common.Result;
import com.lin.reggie.dto.DishDto;

public interface DishService {

    Result<Page> getDishPageList(int page, int pageSize);

    Result<String> addDish(DishDto dishDto,long userId);
}
