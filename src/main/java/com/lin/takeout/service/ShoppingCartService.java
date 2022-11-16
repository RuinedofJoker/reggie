package com.lin.takeout.service;

import com.lin.takeout.common.Result;
import com.lin.takeout.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    Result<String> setCart(ShoppingCart shoppingCart);

    Result<List<ShoppingCart>> getShoppingCart();

    Result<String> removeCart(ShoppingCart shoppingCart);

    Result<String> cleanCart();
}
