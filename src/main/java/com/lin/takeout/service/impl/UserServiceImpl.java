package com.lin.takeout.service.impl;

import com.lin.takeout.common.Result;
import com.lin.takeout.entity.User;
import com.lin.takeout.mapper.UserMapper;
import com.lin.takeout.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public Result<User> checkUserLogin(User user, HttpServletRequest request) {
        User loginUser = user;
        if ((user = userMapper.selectById(user.getId())) != null){
            if (user.getStatus() != 1) return Result.error("用户已被禁用");
            request.getSession().setAttribute("userId",user.getId());
            return Result.success(user);
        }
        loginUser.setStatus(1);
        userMapper.insert(loginUser);
        loginUser = userMapper.selectById(loginUser.getId());
        request.getSession().setAttribute("userId",loginUser.getId());
        return Result.success(loginUser);
    }
}
