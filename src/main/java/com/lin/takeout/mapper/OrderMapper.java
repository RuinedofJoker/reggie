package com.lin.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.takeout.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

    @Update("update orders set status=#{status}")
    int updateStatus(int status);

    @Update("update orders set number=#{id},amount=#{amount} where id=#{id}")
    int updateAmount(long id, BigDecimal amount);
}
