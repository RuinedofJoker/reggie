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
    public Result<List<ShoppingCart>> getCategory(){
        return shoppingCartService.getShoppingCart();
    }

    @PostMapping("/add")
    public Result<String> addCart(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.setCart(shoppingCart);
    }

    @PostMapping("/sub")
    public Result<String> subCart(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.removeCart(shoppingCart);
    }

    @DeleteMapping("/clean")
    public Result<String> cleanCart(HttpServletRequest request){
        return shoppingCartService.cleanCart();
    }
}
