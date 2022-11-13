package com.lin.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.takeout.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Select("select * from dish where name=#{name}")
    Dish selectByName(String name);

    @Update("update dish set status=#{status} where id=#{id}")
    int updateStatusById(int status,long id);
}
