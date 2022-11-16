package com.lin.takeout.service;

import com.lin.takeout.common.Result;
import com.lin.takeout.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    Result<List<AddressBook>> getAddressBookList(String phone);
}
