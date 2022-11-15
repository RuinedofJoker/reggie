package com.lin.takeout.service.impl;

import com.lin.takeout.common.Result;
import com.lin.takeout.entity.User;
import com.lin.takeout.mapper.UserMapper;
import com.lin.takeout.service.UserService;
import com.lin.takeout.util.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Value("${mail.sender}")
    String mailSender;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;

    @Override
    public Result<String> checkUserLogin(String phone, String code, HttpServletRequest request) {
        User loginUser = new User();
        loginUser.setPhone(phone);
        Object codeInSession = redisTemplate.opsForValue().get(phone);
        if (codeInSession == null) return Result.error("请输入已接收验证码的邮箱号");
        loginUser = userMapper.selectByPhone(phone);
        if (loginUser != null){
            if (loginUser.getStatus() != 1) return Result.error("用户已被禁用");
        }
        if (Integer.parseInt(code) == (int)codeInSession){
            if (loginUser == null){
                //注册用户
                User user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                user.setId(new SnowFlakeUtil().getNextId());
                userMapper.insertOne(user);
                request.getSession().setAttribute("user",phone);
                redisTemplate.delete(phone);
                return Result.success("用户注册成功");
            }
            request.getSession().setAttribute("user",phone);
            redisTemplate.delete(phone);
            return Result.success("登录成功");
        }
        return Result.error("请输入正确的验证码");
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
        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
        return Result.success("验证码发送成功");
    }
}
