package com.lin.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.reggie.common.Result;
import com.lin.reggie.entity.Employee;


public interface EmployeeService extends IService<Employee> {

    Result<Employee> login(Employee employee);

    Result getEmployeePageList(int page,int pageSize,String name);

    Result<String> addEmployee(Employee employee,long createUser);

    Result<String> changeEmployeeInfo(Employee employee);

    Result<Employee> getEmployeeById(long id);
}
