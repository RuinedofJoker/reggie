package com.lin.takeout.service;

import com.lin.takeout.common.Result;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    Result<String> checkUserLogin(String phone, String code, HttpServletRequest request);

    Result<String> sendMessage(String phone);
}
