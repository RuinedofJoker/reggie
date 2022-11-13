package com.lin.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;

public interface SetmealService {

    Result<Page> getSetmealPageList(int page, int pageSize);
}
