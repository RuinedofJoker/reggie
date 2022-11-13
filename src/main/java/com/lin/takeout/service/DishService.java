package com.lin.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.DishDto;

public interface DishService {

    Result<Page> getDishPageList(int page, int pageSize);

    Result<String> addDish(DishDto dishDto,long userId);
}
