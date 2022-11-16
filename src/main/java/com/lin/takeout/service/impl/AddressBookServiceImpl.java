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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    AddressBookMapper addressBookMapper;
    @Autowired
    UserMapper userMapper;

    //设置默认地址
    @Override
    @Transactional
    public Result<String> updateDefaultAddress(AddressBook addressBook) {

        //将该用户id下所有地址的默认值设为0
        addressBookMapper.updateIsDefault0(GetIdByThreadLocal.getId());

        //设置当前addressBookId的记录为1
        addressBookMapper.updateIsDefault(GetIdByThreadLocal.getId(),addressBook.getId());

        return Result.success("设置成功");
    }

    //查找默认地址
    @Override
    public Result<AddressBook> getDefaultAddress() {

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(AddressBook::getUserId,GetIdByThreadLocal.getId());
        queryWrapper.eq(AddressBook::getIsDefault,1);

        AddressBook addressBook = addressBookMapper.selectOne(queryWrapper);
        if (addressBook == null)
            return Result.error("未设置默认地址");

        return Result.success(addressBook);
    }

    //根据id获取地址
    @Override
    public Result<AddressBook> getaddressOne(long id) {
        return Result.success(addressBookMapper.selectById(id));
    }

    //修改地址
    @Override
    @Transactional
    public Result<String> updateAddress(AddressBook addressBook) {

        addressBookMapper.deleteById(addressBook.getId());
        addressBook.setUserId(GetIdByThreadLocal.getId());
        addressBookMapper.insert(addressBook);

        return Result.success("插入成功");
    }

    //根据id删除地址
    @Override
    @Transactional
    public Result<String> removeAddress(long ids) {

        addressBookMapper.deleteById(ids);
        return Result.success("删除成功");
    }

    //根据userId查询地址簿
    @Override
    public Result<List<AddressBook>> getAddressBookList() {

        LambdaQueryWrapper<AddressBook> addressBookQueryWrapper = new LambdaQueryWrapper<>();
        addressBookQueryWrapper.eq(AddressBook::getUserId, GetIdByThreadLocal.getId());

        return Result.success(addressBookMapper.selectList(addressBookQueryWrapper));
    }

    //添加地址
    @Override
    @Transactional
    public Result<String> saveAddressBook(AddressBook addressBook) {

        addressBook.setUserId(GetIdByThreadLocal.getId());
        addressBook.setIsDefault(0);

        addressBookMapper.insert(addressBook);

        return Result.success("插入成功");
    }
}
