package com.lin.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Category;
import com.lin.takeout.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public Result<Page> getCategoryList(int page, int pageSize){
        return categoryService.getCategoryPage(page,pageSize);
    }

    /*
        菜品type为1
        套餐type为2
        {分类名称name: "1", type: "2", 排序sort: "1"}
    */
    @PostMapping
    public Result addCategory(@RequestBody Category category){
        return categoryService.saveCategory(category);
    }

    @PutMapping
    public Result editCategory(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @DeleteMapping
    public Result deleCategory(@RequestParam long id) throws Exception{
        return categoryService.removeCategoryById(id);
    }

    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(HttpServletRequest request){

        String type = request.getParameter("type");
        int typeChaged;
        if (StringUtils.isNotEmpty(type)){
            typeChaged = Integer.parseInt(type);
            return categoryService.getCategoryByType(typeChaged);
        } else return categoryService.getCategoryList();
    }
}
