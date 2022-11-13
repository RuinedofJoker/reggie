package com.lin.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.DishDto;
import com.lin.takeout.service.DishService;
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

    @DeleteMapping
    public Result<String> deleteDish(long ids) throws Exception{
        return dishService.deleteDishById(ids);
    }
}
