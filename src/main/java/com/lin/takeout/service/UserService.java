package com.lin.takeout.service;

import com.lin.takeout.common.Result;
import com.lin.takeout.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    public Result<User> checkUserLogin(User user, HttpServletRequest request);
}
