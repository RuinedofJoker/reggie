package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Employee;
import com.lin.takeout.mapper.EmployeeMapper;
import com.lin.takeout.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;


@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;


    @Override
    public Result<Page> getEmployeePageList(int page, int pageSize, String name) {
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeMapper.selectPage(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }

    @Override
    public Result<Employee> login(Employee employee) {
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
        Employee relEmployee = employeeMapper.selectEmployeeByUserName(employee);
        if (relEmployee.getPassword().equals(employee.getPassword()) && relEmployee.getUsername().equals(employee.getUsername()) && relEmployee.getStatus() == 1){
            return Result.success(relEmployee);
        }
        if (relEmployee.getStatus() == 0){
            return Result.error("账号已被封禁");
        }
        return Result.error("登录失败");
    }

    @Override
    public Result<Employee> getEmployeeById(long id) {
        Employee employee = employeeMapper.selectById(id);
        if (employee != null){
            return Result.success(employee);
        }
        return Result.error("未查询到该员工");
    }

    @Override
    public Result<String> changeEmployeeInfo(Employee employee) {
        if (employee.getName() == null){
            if (employeeMapper.updateEmployeeStatusById(employee) != 0){
                return Result.success("修改成功");
            }else {
                return Result.error("修改失败");
            }
        }else {
            if (employeeMapper.updateById(employee) != 0){
                return Result.success("修改成功");
            }else {
                return Result.error("修改失败");
            }
        }

    }

    public Result addEmployee(Employee employee, long createUser){
        Employee checkRepeat = employeeMapper.selectEmployeeByUserName(employee);
        if (checkRepeat != null){
            return Result.error("用户名重复");
        }
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setStatus(1);
        employee.setCreateUser(createUser);
        employee.setUpdateUser(createUser);
        employeeMapper.insert(employee);
        return Result.success("插入成功");
    }
}
