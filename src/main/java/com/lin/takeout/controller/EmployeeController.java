package com.lin.takeout.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Employee;
import com.lin.takeout.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //登录
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        Result result = employeeService.login(employee);
        employee = (Employee)result.getData();
        if (employee.getId() != null)
            request.getSession().setAttribute("employee",employee.getId());
        return result;
    }

    //登出
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    //查询所有员工/查询所有name=此name的员工，并采用分页方式上传
    @GetMapping("/page")
    public Result<Page> getEmployeeList(int page,int pageSize,String name){
        return employeeService.getEmployeePage(page,pageSize,name);
    }

    //添加员工
    @PostMapping
    public Result<String> addEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

    //修改员工状态
    @PutMapping
    public Result<String> editEmployee(@RequestBody Employee employee){
        return employeeService.updateEmployeeInfo(employee);
    }

    //查询员工
    @GetMapping("/{id}")
    public Result<Employee> queryEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }
}
