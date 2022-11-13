package com.lin.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.DishDto;
import com.lin.takeout.entity.Dish;
import com.lin.takeout.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;

    @GetMapping("/page")
    public Result<Page> getDishList(int page,int pageSize,String name){
        return dishService.getDishPageList(page,pageSize,name);
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

    @GetMapping("/{id}")
    public Result<DishDto> queryDishById(@PathVariable long id){
        return dishService.getDishById(id);
    }

    @PutMapping
    public Result<String> putDish(@RequestBody DishDto dishDto, HttpServletRequest request) throws Exception {
        long userId = (long) request.getSession().getAttribute("employee");
        return dishService.changeDish(dishDto,userId);
    }

    @PostMapping("/status/{status}")
    public Result<String> changeDishStatus(@PathVariable int status,long ids){
        return dishService.changeDishStatus(status,ids);
    }

    @GetMapping("/list")
    public Result<List<Dish>> getDishByCategoryId(long categoryId){
        return dishService.getDishByCategoryId(categoryId);
    }
}
