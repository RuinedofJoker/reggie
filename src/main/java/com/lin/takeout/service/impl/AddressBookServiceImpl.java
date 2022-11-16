package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.AddressBook;
import com.lin.takeout.entity.User;
import com.lin.takeout.mapper.AddressBookMapper;
import com.lin.takeout.mapper.UserMapper;
import com.lin.takeout.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    AddressBookMapper addressBookMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public Result<List<AddressBook>> getAddressBookList(String phone) {

        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getPhone,phone);
        User user = userMapper.selectOne(userQueryWrapper);

        LambdaQueryWrapper<AddressBook> addressBookQueryWrapper = new LambdaQueryWrapper<>();
        addressBookQueryWrapper.eq(AddressBook::getUserId,user.getId());
        return Result.success(addressBookMapper.selectList(addressBookQueryWrapper));

    }
}
