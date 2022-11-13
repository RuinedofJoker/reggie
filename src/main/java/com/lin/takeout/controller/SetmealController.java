package com.lin.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.SetmealDto;
import com.lin.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @GetMapping("/page")
    public Result<Page> getSetmealList(int page,int pageSize,String name){
        return setmealService.getSetmealPageList(page,pageSize,name);
    }

    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDto setmealDto, HttpServletRequest request){
        long userId = (long) request.getSession().getAttribute("employee");
        return setmealService.setSetmeal(setmealDto,userId);
    }

    @GetMapping("/{id}")
    public Result<SetmealDto> getSetmealById(@PathVariable long id){
        return setmealService.getSetmealById(id);
    }

    @PutMapping
    public Result<String> changeSetmeal(@RequestBody SetmealDto setmealDto, HttpServletRequest request) throws Exception{
        long userId = (long) request.getSession().getAttribute("employee");
        return setmealService.changeSetmealById(setmealDto,userId);
    }

    @DeleteMapping
    public Result<String> deleteSetmeal(long ids) throws Exception{
        return setmealService.deleteSetmealById(ids);
    }

    @PostMapping("/status/{status}")
    public Result<String> changeSetmealStatus(@PathVariable int status, String ids, HttpServletRequest request){
        long userId = (long) request.getSession().getAttribute("employee");
        return setmealService.changeSetmealStatus(status,ids,userId);
    }
}
