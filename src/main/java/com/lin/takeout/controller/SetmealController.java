package com.lin.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @GetMapping("/page")
    public Result<Page> getSetmealList(int page,int pageSize){
        return setmealService.getSetmealPageList(page,pageSize);
    }
}
