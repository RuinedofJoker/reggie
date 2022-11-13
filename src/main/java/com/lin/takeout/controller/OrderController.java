package com.lin.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/page")
    public Result<Page> getOrderList(int page,int pageSize){
        return orderService.getOrderPageList(page,pageSize);
    }
}
