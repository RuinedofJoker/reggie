package com.lin.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Employee;


public interface EmployeeService extends IService<Employee> {

    Result<Employee> login(Employee employee);

    Result getEmployeePageList(int page,int pageSize,String name);

    Result<String> addEmployee(Employee employee,long createUser);

    Result<String> changeEmployeeInfo(Employee employee);

    Result<Employee> getEmployeeById(long id);
}
