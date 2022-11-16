package com.lin.takeout.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Orders;

import java.util.Map;

public interface OrderService {

    Result<Page> getOrderPage(int page, int pageSize, String number, String beginTime, String endTime);

    Result<String> updateStatus(Orders orders);

    Result<String> saveOrder(Map map);

    Result<String> saveOrderAgain(long id);
}
