package com.lin.takeout.service.impl;

import com.lin.takeout.common.GetIdByThreadLocal;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.ShoppingCart;
import com.lin.takeout.mapper.DishMapper;
import com.lin.takeout.mapper.SetmealMapper;
import com.lin.takeout.mapper.UserMapper;
import com.lin.takeout.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {


    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;

    //添加菜品到购物车
    @Override
    @Transactional
    public Result<String> setCart(ShoppingCart shoppingCart) {

    /*
        Redis ShoppingCart 格式：
        使用hash格式存储
        key为userId从session.getAttribute("user")获取
        hashKey为菜品id/套餐id+口味（+为分割符，套餐没有口味但也有+）
        value就是ShoppingCart对象
    */
        ShoppingCart localCart = null;
        shoppingCart.setCreateTime(LocalDateTime.now());

        shoppingCart.setUserId(GetIdByThreadLocal.getId());

        if (shoppingCart.getSetmealId() != null){

            shoppingCart.setSetmealId(setmealMapper.selectByName(shoppingCart.getName()).getId());
            localCart = (ShoppingCart) redisTemplate.opsForHash().get(GetIdByThreadLocal.getId(), shoppingCart.getSetmealId()+"+");

            if (localCart == null){

                shoppingCart.setNumber(1);
                shoppingCart.setId(shoppingCart.getSetmealId()+"+");
                redisTemplate.opsForHash().put(GetIdByThreadLocal.getId(),shoppingCart.getSetmealId()+"+",shoppingCart);

            }else {

                shoppingCart.setNumber(localCart.getNumber()+1);
                redisTemplate.opsForHash().put(GetIdByThreadLocal.getId(),shoppingCart.getSetmealId()+"+",shoppingCart);

            }

        }else if (shoppingCart.getDishId() != null){

            shoppingCart.setDishId(dishMapper.selectByName(shoppingCart.getName()).getId());
            localCart = (ShoppingCart) redisTemplate.opsForHash().get(GetIdByThreadLocal.getId(), shoppingCart.getDishId()+"+"+shoppingCart.getDishFlavor());
            if (localCart == null){

                shoppingCart.setNumber(1);
                shoppingCart.setId(shoppingCart.getDishId()+"+"+shoppingCart.getDishFlavor());
                redisTemplate.opsForHash().put(GetIdByThreadLocal.getId(),shoppingCart.getDishId()+"+"+shoppingCart.getDishFlavor(),shoppingCart);

            }else {

                shoppingCart.setNumber(localCart.getNumber()+1);
                redisTemplate.opsForHash().put(GetIdByThreadLocal.getId(),shoppingCart.getDishId()+"+"+shoppingCart.getDishFlavor(),shoppingCart);

            }
        }

        redisTemplate.expire(GetIdByThreadLocal.getId(), 2, TimeUnit.DAYS);
        return Result.success("添加购物车成功");
    }

    //清空购物车
    @Override
    public Result<String> cleanCart() {
        redisTemplate.delete(GetIdByThreadLocal.getId());
        return Result.success("删除成功");
    }

    //根据购物车菜品id清除购物车
    @Override
    @Transactional
    public Result<String> removeCart(ShoppingCart shoppingCart) {

        Map<String,ShoppingCart> map = redisTemplate.opsForHash().entries(GetIdByThreadLocal.getId());

        boolean isSetmeal = true;
        if (shoppingCart.getSetmealId() == null) isSetmeal=false;

        for (String key:map.keySet()) {

            if (isSetmeal){
                if (key.equals(shoppingCart.getSetmealId()+"+")){
                    redisTemplate.opsForHash().delete(GetIdByThreadLocal.getId(),key);
                }
            }else {
                //由于前端页面设计缺陷，无法传递菜品偏好，因此直接删除所有相同菜品
/*                if (key.equals(shoppingCart.getDishId()+"+"+shoppingCart.getDishFlavor())){
                    redisTemplate.opsForHash().delete(phone,key);
                }*/
                String[] splitByAdd = key.split("\\+");
                if (splitByAdd[0].equals(shoppingCart.getDishId()+"")){
                    redisTemplate.opsForHash().delete(GetIdByThreadLocal.getId(),key);
                }
            }
        }
        return Result.success("删除成功");
    }

    //查询购物车内所有物品
    @Override
    public Result<List<ShoppingCart>> getShoppingCart() {

        Map<String,ShoppingCart> map = redisTemplate.opsForHash().entries(GetIdByThreadLocal.getId());
        List<ShoppingCart> list = new ArrayList<>();
        for (String key:map.keySet()) {

            ShoppingCart shoppingCart = map.get(key);
            list.add(shoppingCart);

        }
        return Result.success(list);
    }
}
