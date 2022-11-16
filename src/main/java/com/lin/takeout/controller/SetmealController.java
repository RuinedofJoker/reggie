package com.lin.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.SetmealDto;
import com.lin.takeout.entity.Setmeal;
import com.lin.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @GetMapping("/page")
    public Result<Page> getSetmealPage(int page,int pageSize,String name){
        return setmealService.getSetmealPage(page,pageSize,name);
    }

    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        return setmealService.saveSetmeal(setmealDto);
    }

    @GetMapping("/{id}")
    public Result<SetmealDto> querySetmealById(@PathVariable long id){
        return setmealService.getSetmealById(id);
    }

    @PutMapping
    public Result<String> editSetmeal(@RequestBody SetmealDto setmealDto) throws Exception{
        return setmealService.updateSetmealById(setmealDto);
    }

    @DeleteMapping
    public Result<String> deleteSetmeal(String ids) throws Exception{
        return setmealService.deleteSetmealById(ids);
    }

    @PostMapping("/status/{status}")
    public Result<String> setmealStatusByStatus(@PathVariable int status, String ids){
        return setmealService.updateSetmealStatus(status,ids);
    }

    @GetMapping("/list")
    public Result<List<Setmeal>> getCategory(Setmeal setmeal){
        return setmealService.getCategoryList(setmeal);
    }
}
