package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.GetIdByThreadLocal;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.*;
import com.lin.takeout.mapper.AddressBookMapper;
import com.lin.takeout.mapper.OrderDetailMapper;
import com.lin.takeout.mapper.OrderMapper;
import com.lin.takeout.service.OrderService;
import com.lin.takeout.service.ShoppingCartService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    AddressBookMapper addressBookMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    ShoppingCartService shoppingCartService;

    //订单的分页查询
    @Override
    public Result<Page> getOrderPage(int page, int pageSize, String number, String beginTime, String endTime) {

        //定义订单的时间范围
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime beginTimeChanged = null;
        LocalDateTime endTimeChanged = null;

        Page pageInfo = new Page<Dish>(page,pageSize);
        if (StringUtils.isNotEmpty(beginTime))
            beginTimeChanged = LocalDateTime.parse(beginTime,dateFormat);
        if (StringUtils.isNotEmpty(endTime))
            endTimeChanged = LocalDateTime.parse(endTime,dateFormat);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(number),Orders::getNumber,number);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        queryWrapper.ge(StringUtils.isNotEmpty(beginTime),Orders::getOrderTime,beginTimeChanged);
        queryWrapper.le(StringUtils.isNotEmpty(endTime),Orders::getOrderTime,endTimeChanged);

        orderMapper.selectPage(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }

    //修改订单状态
    @Override
    public Result<String> updateStatus(Orders orders) {
        orderMapper.updateStatus(orders.getStatus());
        return Result.success("修改成功");
    }

    //添加订单
    @Override
    @Transactional
    public Result<String> saveOrder(Map map) {

        /*
            生成订单插入到订单表中
            根据userId将购物车中的数据取出（使用Map存储,key为菜品名+偏好/套餐名+,value为ShoppingCart对象）
            将ShoppingCart对象转换成OrderDetail对象（两者只有ShoppingCart多了一个创建时间，其他一样）
            设置OrderDetail的订单号后插入到订单明细表
        */

        //计算合计金额
        BigDecimal amount = new BigDecimal(0);

        //设置订单信息
        Orders orders = new Orders();
        AddressBook addressBook = addressBookMapper.selectById(Long.parseLong((String) map.get("addressBookId")));

        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());

        orders.setAddressBookId(Long.parseLong((String) map.get("addressBookId")));
        orders.setUserId(GetIdByThreadLocal.getId());
        orders.setStatus(2);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPayMethod((int) map.get("payMethod"));
        orders.setRemark((String) map.get("remark"));
        orders.setAmount(new BigDecimal(0));

        orderMapper.insert(orders);
        orders = orderMapper.selectById(orders.getId());

        Map<String,ShoppingCart> list = redisTemplate.opsForHash().entries(GetIdByThreadLocal.getId());

        for (String key:list.keySet()){

            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(list.get(key),orderDetail);
            orderDetail.setOrderId(orders.getId());
            amount = amount.add(orderDetail.getAmount());

            orderDetailMapper.insert(orderDetail);

        }

        orderMapper.updateAmount(orders.getId(),amount);

        redisTemplate.delete(GetIdByThreadLocal.getId());

        return Result.success("交易完成");
    }

    //再次获取订单
    @Override
    @Transactional
    public Result<String> saveOrderAgain(long id) {

        Map map = new HashMap();
        Orders orders = orderMapper.selectById(id);

        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,id);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(queryWrapper);

        for (int i = 0;i < orderDetailList.size();i++){

            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetailList.get(i),shoppingCart);
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCartService.setCart(shoppingCart);

        }

        map.put("addressBookId",orders.getAddressBookId()+"");
        map.put("remark",orders.getRemark());
        map.put("payMethod",orders.getPayMethod());

        return saveOrder(map);
    }
}
