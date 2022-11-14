package com.lin.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.takeout.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where phone=#{phone}")
    User selectByPhone(String phone);

    @Insert("insert into user (id,phone,status) values(#{id},#{phone},#{status})")
    int insertOne(User user);
}
