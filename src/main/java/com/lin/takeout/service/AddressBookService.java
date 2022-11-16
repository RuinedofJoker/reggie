package com.lin.takeout.service;

import com.lin.takeout.common.Result;
import com.lin.takeout.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    Result<List<AddressBook>> getAddressBookList();

    Result<String> saveAddressBook(AddressBook addressBook);

    Result<String> updateDefaultAddress(AddressBook addressBook);

    Result<AddressBook> getaddressOne(long id);

    Result<String> updateAddress(AddressBook addressBook);

    Result<String> removeAddress(long ids);

    Result<AddressBook> getDefaultAddress();
}
