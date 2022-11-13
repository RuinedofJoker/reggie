package com.lin.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Category;
import com.lin.takeout.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/list")
    public Result<List<Category>> getCategoryByType(int type){
        return categoryService.getCategoryByType(type);
    }
}
