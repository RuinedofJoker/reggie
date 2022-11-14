package com.lin.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    /*
        page:
        1
        pageSize:
        10
        number:
        11
        beginTime:
        2022-11-16 00:00:00
        endTime:
        2022-12-16 23:59:59
    */
    @GetMapping("/page")
    public Result<Page> getOrderList(int page, int pageSize, String number, String beginTime, String endTime){
        return orderService.getOrderPageList(page,pageSize,number,beginTime,endTime);
    }

    @PutMapping
    public Result changeStatus(String id,String status){
        return Result.error("功能未开放");
    }
}
