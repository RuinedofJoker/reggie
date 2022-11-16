package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Dish;
import com.lin.takeout.entity.Orders;
import com.lin.takeout.mapper.OrderMapper;
import com.lin.takeout.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    //订单的分页查询
    @Override
    public Result<Page> getOrderPage(int page, int pageSize, String number, String beginTime, String endTime) {

        //定义订单的时间范围
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime beginTimeChanged = null;
        LocalDateTime endTimeChanged = null;

        Page pageInfo = new Page<Dish>(page,pageSize);
        if (StringUtils.isNotEmpty(beginTime))
            beginTimeChanged = LocalDateTime.parse(beginTime,dateFormat);
        if (StringUtils.isNotEmpty(endTime))
            endTimeChanged = LocalDateTime.parse(endTime,dateFormat);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(number),Orders::getNumber,number);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        queryWrapper.ge(StringUtils.isNotEmpty(beginTime),Orders::getOrderTime,beginTimeChanged);
        queryWrapper.le(StringUtils.isNotEmpty(endTime),Orders::getOrderTime,endTimeChanged);

        orderMapper.selectPage(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }

    //修改订单状态
    @Override
    public Result<String> updateStatus(Orders orders) {
        orderMapper.updateStatus(orders.getStatus());
        return Result.success("修改成功");
    }
}
