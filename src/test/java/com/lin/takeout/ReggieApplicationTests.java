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

/*        MyThread myThread1 = new MyThread();
        MyThread myThread2 = new MyThread();
        MyThread myThread3 = new MyThread();

        myThread1.setName("1号线程");
        myThread1.setId(1);

        myThread2.setName("2号线程");
        myThread2.setId(2);

        myThread3.setName("3号线程");
        myThread3.setId(3);

        myThread1.start();
        myThread2.start();
        myThread3.start();*/
    }

}
