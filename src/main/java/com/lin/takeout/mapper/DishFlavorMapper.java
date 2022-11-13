package com.lin.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.takeout.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

    @Delete("delete from dish_flavor where dish_id=#{id}")
    int deleteByDishId(long id);
}
