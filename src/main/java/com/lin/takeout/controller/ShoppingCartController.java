package com.lin.takeout.controller;


import com.lin.takeout.common.Result;
import com.lin.takeout.entity.ShoppingCart;
import com.lin.takeout.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;
    @GetMapping("/list")
    public Result<List<ShoppingCart>> getCategory(HttpServletRequest request){
        //String userId = (String) request.getSession().getAttribute("user");
        String userId = "616401600@qq.com";
        return shoppingCartService.getshoppingCart(userId);
    }

    /*
                amount:item.price/100,//金额
                dishFlavor:item.dishFlavor,//口味  如果没有传undefined
                dishId:undefined,//菜品id
                setmealId:undefined,//套餐id
                name:item.name,
                image:item.image
    */
    @PostMapping("/add")
    public Result<String> addCart(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request){
        String user = (String) request.getSession().getAttribute("user");
        return shoppingCartService.setCart(shoppingCart,user);
    }

    @PostMapping("/sub")
    public Result<String> subCart(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request){
        String user = (String) request.getSession().getAttribute("user");
        return shoppingCartService.removeCart(shoppingCart,user);
    }

    @DeleteMapping("/clean")
    public Result<String> cleanCart(HttpServletRequest request){
        String user = (String) request.getSession().getAttribute("user");
        return shoppingCartService.cleanCart(user);
    }
}
