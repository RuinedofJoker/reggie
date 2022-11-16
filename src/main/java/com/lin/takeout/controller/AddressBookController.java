package com.lin.takeout.controller;

import com.lin.takeout.common.Result;
import com.lin.takeout.entity.AddressBook;
import com.lin.takeout.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    @GetMapping("/list")
    public Result<List<AddressBook>> addressList(){
        return addressBookService.getAddressBookList();
    }

    @PostMapping
    public Result<String> addAddress(@RequestBody AddressBook addressBook){
        return addressBookService.saveAddressBook(addressBook);
    }

    @PutMapping("/default")
    public Result<String> setDefaultAddress(@RequestBody AddressBook addressBook){
        return addressBookService.updateDefaultAddress(addressBook);
    }

    @GetMapping("/{id}")
    public Result<AddressBook> addressFindOne(@PathVariable long id){
        return addressBookService.getaddressOne(id);
    }

    @PutMapping
    public Result<String> updateAddress(@RequestBody AddressBook addressBook){
        return addressBookService.updateAddress(addressBook);
    }

    @DeleteMapping
    public Result<String> deleteAddress(long ids){
        return addressBookService.removeAddress(ids);
    }

    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddress(){
        return addressBookService.getDefaultAddress();
    }
}
