package com.lin.reggie.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.reggie.common.Result;
import com.lin.reggie.mapper.CategoryMapper;
import com.lin.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public Result<Page> getPage(int page, int pageSize) {
        Page pageInfo = new Page(page,pageSize);
        categoryMapper.selectPage(pageInfo,null);

        return Result.success(pageInfo);
    }
}
