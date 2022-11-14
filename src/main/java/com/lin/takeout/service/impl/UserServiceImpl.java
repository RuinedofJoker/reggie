package com.lin.takeout.service.impl;

import com.lin.takeout.common.Result;
import com.lin.takeout.entity.User;
import com.lin.takeout.mapper.UserMapper;
import com.lin.takeout.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Value("${mail.sender}")
    String mailSender;
    @Autowired
    JavaMailSender javaMailSender;
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

    @Override
    public Result<String> sendMessage(String phone) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailSender);
        mailMessage.setTo(phone);
        mailMessage.setSubject("登录验证码");
        Integer code =null;
        code = new Random().nextInt(9999);//生成随机数，最大为9999
        if(code < 1000){
            code = code + 1000;//保证随机数为4位数字
        }
        mailMessage.setText(code+"");
        javaMailSender.send(mailMessage);
        //将phone:code存入redis
        return Result.success("验证码发送成功");
    }
}
