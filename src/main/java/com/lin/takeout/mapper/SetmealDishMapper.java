package com.lin.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.takeout.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {

    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> selectByDishId(long id);

    @Select("select * from setmeal_dish where name=#{name}")
    SetmealDish selectByName(String name);

    @Delete("delete from setmeal_dish where setmeal_id=#{id}")
    int deleteBySetmealId(long id);
}
