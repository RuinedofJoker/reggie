package com.lin.reggie;

import com.lin.reggie.mapper.CategoryMapper;
import com.lin.reggie.mapper.EmployeeMapper;
import com.lin.reggie.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Test
    void contextLoads() {
/*        Employee employee = new Employee();
        employee.setId(1590674811368042497L);
        employee.setStatus(1);
        System.out.println(employeeMapper.updateEmployeeStatusById(employee));*/

/*        System.out.println(categoryMapper.selectBySort(1));*/

        /*System.out.println(categoryMapper.selectByType(1));*/
    }

}
