package com.lin.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.reggie.common.Result;
import com.lin.reggie.entity.Category;
import com.lin.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public Result<Page> getCategoryList(int page, int pageSize){
        return categoryService.getPage(page,pageSize);
    }

/*  菜品type为1
    套餐type为2
    {分类名称name: "1", type: "2", 排序sort: "1"}*/
    @PostMapping
    public Result addCategory(@RequestBody Category category, HttpServletRequest request){
        Long createUserId = (Long) request.getSession().getAttribute("employee");
        category.setCreateUser(createUserId);
        category.setUpdateUser(createUserId);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        return categoryService.setCategory(category);
    }

    @PutMapping
    public Result changeCategoryImfo(@RequestBody Category category, HttpServletRequest request){
        Long createUserId = (Long) request.getSession().getAttribute("employee");
        category.setCreateUser(createUserId);
        category.setUpdateUser(createUserId);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        return categoryService.changeCategory(category);
    }

    @DeleteMapping
    public Result deleteCategory(@RequestParam long id){
        return categoryService.deleteCategoryById(id);
    }
}
