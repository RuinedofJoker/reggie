package com.lin.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.DishDto;
import com.lin.takeout.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;

    @GetMapping("/page")
    public Result<Page> getDishPage(int page,int pageSize,String name){
        return dishService.getDishPageList(page,pageSize,name);
    }

    @PostMapping
    public Result<String> addDish(@RequestBody DishDto dishDto){
        return dishService.saveDish(dishDto);
    }

    @DeleteMapping
    public Result<String> deleteDish(String ids) throws Exception{
        return dishService.deleteDishById(ids);
    }

    @GetMapping("/{id}")
    public Result<DishDto> queryDishById(@PathVariable long id){
        return dishService.getDishById(id);
    }

    @PutMapping
    public Result<String> editDish(@RequestBody DishDto dishDto) throws Exception {
        return dishService.updateDish(dishDto);
    }

    @PostMapping("/status/{status}")
    public Result<String> dishStatusByStatus(@PathVariable int status,String ids){
        return dishService.updateDishStatus(status,ids);
    }

    @GetMapping("/list")
    public Result<List<DishDto>> queryDishList(long categoryId){
        return dishService.getDishByCategoryId(categoryId);
    }
}
