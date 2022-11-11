package com.lin.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.reggie.common.Result;

public interface CategoryService {

    Result<Page> getPage(int page,int pageSize);
}
