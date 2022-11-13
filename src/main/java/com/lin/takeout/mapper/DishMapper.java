package com.lin.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.takeout.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Select("select * from dish where name=#{name}")
    Dish selectByName(String name);
}
