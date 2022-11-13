package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Dish;
import com.lin.takeout.mapper.OrderMapper;
import com.lin.takeout.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Override
    public Result<Page> getOrderPageList(int page, int pageSize) {        Page pageInfo = new Page<Dish>(page,pageSize);
        orderMapper.selectPage(pageInfo,null);
        return Result.success(pageInfo);
    }
}
