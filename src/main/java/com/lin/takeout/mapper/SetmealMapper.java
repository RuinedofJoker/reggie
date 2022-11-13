package com.lin.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.takeout.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    @Select("select * from setmeal where name=#{name}")
    Setmeal selectByName(String name);

    @Update("update setmeal set status=#{status},update_time=#{updateTime},update_user=#{userId} where id=#{id}")
    int updateStatusById(int status, long id, LocalDateTime updateTime,long userId);
}
