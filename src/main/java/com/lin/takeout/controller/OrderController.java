package com.lin.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Orders;
import com.lin.takeout.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/page")
    public Result<Page> getOrderDetailPage(int page, int pageSize, String number, String beginTime, String endTime){
        return orderService.getOrderPage(page,pageSize,number,beginTime,endTime);
    }

    @PutMapping
    public Result<String> editOrderDetail(@RequestBody Orders orders){
        return orderService.updateStatus(orders);
    }

    @GetMapping("/userPage")
    public Result<Page> orderPaging(int page, int pageSize){
        return orderService.getOrderPage(page,pageSize,null,null,null);
    }

    @PostMapping("/submit")
    public Result<String> addOrder(@RequestBody Map map){
        return orderService.saveOrder(map);
    }

    @PostMapping("/again")
    public Result<String> orderAgain(@RequestBody Orders orders){
        return orderService.saveOrderAgain(orders.getId());
    }
}
