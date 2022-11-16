package com.lin.takeout.service;

import com.lin.takeout.common.Result;
import com.lin.takeout.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    Result<String> setCart(ShoppingCart shoppingCart,String phone);

    Result<List<ShoppingCart>> getShoppingCart(String phone);

    Result<String> removeCart(ShoppingCart shoppingCart,String phone);

    Result<String> cleanCart(String phone);
}
