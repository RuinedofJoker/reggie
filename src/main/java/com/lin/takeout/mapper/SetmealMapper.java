package com.lin.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.takeout.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    @Select("select * from setmeal where name=#{name}")
    Setmeal selectByName(String name);

    @Update("update setmeal set status=#{status} where id=#{id}")
    int updateStatusById(int status, long id);
}
