package com.lin.takeout.common;

import com.lin.takeout.entity.Category;
import com.lin.takeout.entity.Setmeal;
import com.lin.takeout.mapper.CategoryMapper;
import com.lin.takeout.mapper.DishMapper;
import com.lin.takeout.mapper.SetmealMapper;
import com.lin.takeout.service.CategoryService;
import com.lin.takeout.service.DishService;
import com.lin.takeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CacheScheduled {

    @Autowired
    DishService dishService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    SetmealService setmealService;
    @Autowired
    CategoryMapper categoryMapper;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void cacheSelectTask(){
        List<Category> categories = categoryMapper.selectAll();
        categoryService.getCategoryList();
        for (Category category : categories) {
            dishService.getDishByCategoryId(category.getId());
            Setmeal setmeal = new Setmeal();
            setmeal.setCategoryId(category.getId());
            setmealService.getCategoryList(setmeal);
        }
        log.info("缓存已更新");
    }
}
