package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.User;
import com.lin.takeout.mapper.UserMapper;
import com.lin.takeout.service.UserService;
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

    //用户登录检查
    @Override
    public Result<String> checkUserLogin(String phone, String code, HttpServletRequest request) {

        boolean isLogin = false;
        User loginUser = new User();
        loginUser.setPhone(phone);

        int checkCode = Integer.parseInt(code);
        //如果redis中存在数据且验证码也相同则说明登录成功
        int codeInSession = (Integer) redisTemplate.opsForValue().get(phone);
        if (checkCode == codeInSession){
            isLogin = true;
            redisTemplate.delete(phone);
        }

        //判断数据库中是否存在当前手机号的用户，如果没有则创建一个用户
        //user表规定：phone不能重复
        if (isLogin){

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userMapper.selectOne(queryWrapper);

            //用户第一次登录，创建用户
            if (user.getId() == null){

                loginUser.setName(loginUser.getPhone());
                loginUser.setStatus(1);
                userMapper.insert(loginUser);
                request.getSession().setAttribute("user",userMapper.selectByPhone(phone).getId());
                return Result.success("登录成功");

            }else {

                if (user.getStatus() == 1){
                    request.getSession().setAttribute("user",user.getId());
                    return Result.success("登录成功");
                }

                return Result.error("用户已被禁用");
            }

        }else {
            return Result.error("登录失败");
        }
    }

    //发送验证码
    @Override
    public Result<String> sendMessage(String phone) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailSender);
        mailMessage.setTo(phone);
        mailMessage.setSubject("登录验证码");

        Integer code =null;
        code = new Random().nextInt(9999);//生成随机数，最大为9999
        if(code < 1000)
            code = code + 1000;//保证随机数为4位数字

        mailMessage.setText(code+"");
        javaMailSender.send(mailMessage);

        //将phone:code存入redis
        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

        return Result.success("验证码发送成功");
    }
}
