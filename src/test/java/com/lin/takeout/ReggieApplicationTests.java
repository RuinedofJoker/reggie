package com.lin.takeout;

import com.lin.takeout.mapper.CategoryMapper;
import com.lin.takeout.mapper.EmployeeMapper;
import com.lin.takeout.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Test
    void contextLoads() {

    }

}
