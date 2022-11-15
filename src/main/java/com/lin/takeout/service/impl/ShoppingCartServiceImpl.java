package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.ShoppingCart;
import com.lin.takeout.entity.User;
import com.lin.takeout.mapper.DishMapper;
import com.lin.takeout.mapper.SetmealMapper;
import com.lin.takeout.mapper.UserMapper;
import com.lin.takeout.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public Result<String> setCart(ShoppingCart shoppingCart,String phone) {

    /*
        Redis ShoppingCart 格式：
        使用hash格式存储
        key为phone从session.getAttribute("user")获取
        hashKey为菜品id/套餐id+口味（+为分割符，套餐没有口味但也有+）
        value就是ShoppingCart对象
    */
        /*
            amount: 78
            dishFlavor: "不要辣,不辣"
            dishId: "1397849739276890114"
            image: "f966a38e-0780-40be-bb52-5699d13cb3d9.jpg"
            name: "辣子鸡"
        */
        ShoppingCart localCart = null;
        shoppingCart.setCreateTime(LocalDateTime.now());

        User user = null;
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper();
        userQueryWrapper.eq(User::getPhone,phone);
        user = userMapper.selectOne(userQueryWrapper);
        shoppingCart.setUserId(user.getId());

        if (shoppingCart.getSetmealId() != null){

            shoppingCart.setSetmealId(setmealMapper.selectByName(shoppingCart.getName()).getId());
            localCart = (ShoppingCart) redisTemplate.opsForHash().get(phone, shoppingCart.getSetmealId()+"+");

            if (localCart == null){

                shoppingCart.setNumber(1);
                redisTemplate.opsForHash().put(phone,shoppingCart.getSetmealId()+"+",shoppingCart);

            }else {

                shoppingCart.setNumber(localCart.getNumber()+1);
                redisTemplate.opsForHash().put(phone,shoppingCart.getSetmealId()+"+",shoppingCart);

            }

        }else if (shoppingCart.getDishId() != null){

            shoppingCart.setDishId(dishMapper.selectByName(shoppingCart.getName()).getId());
            localCart = (ShoppingCart) redisTemplate.opsForHash().get(phone, shoppingCart.getDishId()+"+"+shoppingCart.getDishFlavor());
            if (localCart == null){

                shoppingCart.setNumber(1);
                redisTemplate.opsForHash().put(phone,shoppingCart.getDishId()+"+"+shoppingCart.getDishFlavor(),shoppingCart);

            }else {

                shoppingCart.setNumber(localCart.getNumber()+1);
                redisTemplate.opsForHash().put(phone,shoppingCart.getDishId()+"+"+shoppingCart.getDishFlavor(),shoppingCart);

            }
        }
        return Result.success("添加购物车成功");
    }

    @Override
    public Result<String> cleanCart(String phone) {
        redisTemplate.delete(phone);
        return Result.success("删除成功");
    }

    @Override
    public Result<String> removeCart(ShoppingCart shoppingCart, String phone) {

        Map<String,ShoppingCart> map = redisTemplate.opsForHash().entries(phone);

        boolean isSetmeal = true;
        if (shoppingCart.getSetmealId() == null) isSetmeal=false;

        for (String key:map.keySet()) {

            if (isSetmeal){
                if (key.equals(shoppingCart.getSetmealId()+"+")){
                    redisTemplate.opsForHash().delete(phone,key);
                }
            }else {
                //由于前端页面设计缺陷，无法传递菜品偏好，因此直接删除所有相同菜品
                /*if (key.equals(shoppingCart.getDishId()+"+"+shoppingCart.getDishFlavor())){
                    redisTemplate.opsForHash().delete(phone,key);
                }*/
                String[] splitByAdd = key.split("\\+");
                if (splitByAdd[0].equals(shoppingCart.getDishId()+"")){
                    redisTemplate.opsForHash().delete(phone,key);
                }
            }
        }
        return Result.success("删除成功");
    }

    @Override
    public Result<List<ShoppingCart>> getshoppingCart(String phone) {

        Map<String,ShoppingCart> map = redisTemplate.opsForHash().entries(phone);
        List<ShoppingCart> list = new ArrayList<>();
        for (String key:map.keySet()) {

            ShoppingCart shoppingCart = map.get(key);
            list.add(shoppingCart);

        }
        return Result.success(list);
    }
}
