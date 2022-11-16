package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.takeout.common.GetIdByThreadLocal;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.AddressBook;
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

    //根据userId查询地址簿
    @Override
    public Result<List<AddressBook>> getAddressBookList() {

        LambdaQueryWrapper<AddressBook> addressBookQueryWrapper = new LambdaQueryWrapper<>();
        addressBookQueryWrapper.eq(AddressBook::getUserId, GetIdByThreadLocal.getId());
        return Result.success(addressBookMapper.selectList(addressBookQueryWrapper));

    }
}
