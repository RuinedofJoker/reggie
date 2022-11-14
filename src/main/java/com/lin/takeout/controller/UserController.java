package com.lin.takeout.controller;

import com.lin.takeout.common.Result;
import com.lin.takeout.entity.User;
import com.lin.takeout.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Result<User> login(@RequestBody User user, String code, HttpServletRequest request){
        return userService.checkUserLogin(user,request);
    }

    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user){
        return Result.success("");
    }
}
