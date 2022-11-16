package com.lin.takeout.controller;

import com.lin.takeout.common.Result;
import com.lin.takeout.entity.AddressBook;
import com.lin.takeout.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    @GetMapping("/list")
    public Result<List<AddressBook>> addressList(HttpServletRequest request){
        String phone = (String) request.getSession().getAttribute("user");
        return addressBookService.getAddressBookList(phone);
    }

    @PostMapping
    public void addAddress(@RequestBody AddressBook addressBook){
        return;
    }
}
