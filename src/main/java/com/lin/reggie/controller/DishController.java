package com.lin.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.reggie.common.Result;
import com.lin.reggie.dto.DishDto;
import com.lin.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;

    @GetMapping("/page")
    public Result<Page> getDishList(int page,int pageSize){
        return dishService.getDishPageList(page,pageSize);
    }

    @PostMapping
    public Result<String> addDish(@RequestBody DishDto dishDto, HttpServletRequest request){
        long userId = (long) request.getSession().getAttribute("employee");
        return dishService.addDish(dishDto,userId);
    }
}
