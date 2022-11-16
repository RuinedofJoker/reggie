package com.lin.takeout.controller;

import com.lin.takeout.common.Result;
import com.lin.takeout.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody Map map, HttpServletRequest request){
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        return userService.checkUserLogin(phone,code,request);

        //模拟简化登录
/*        long userId = 1579243405687934976L;
        request.getSession().setAttribute("user",userId);
        return Result.success("登录成功");*/
    }

    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody Map map){
        String phone = (String) map.get("phone");
        return userService.sendMessage(phone);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return Result.success("退出成功");
    }
}
