package com.lin.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Employee;


public interface EmployeeService extends IService<Employee> {

    Result<Employee> login(Employee employee);

    Result getEmployeePage(int page,int pageSize,String name);

    Result<String> saveEmployee(Employee employee);

    Result<String> updateEmployeeInfo(Employee employee);

    Result<Employee> getEmployeeById(long id);
}
