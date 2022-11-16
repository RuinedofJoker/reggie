package com.lin.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.takeout.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

    @Update("update address_book set is_default=0 where user_id=#{userId}")
    int updateIsDefault0(long userId);

    @Update("update address_book set is_default=1 where id=#{id} and user_id=#{userId}")
    int updateIsDefault(long userId,long id);

}
