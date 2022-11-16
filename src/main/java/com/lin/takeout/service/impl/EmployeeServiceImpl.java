package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.takeout.common.GetIdByThreadLocal;
import com.lin.takeout.common.Result;
import com.lin.takeout.entity.Employee;
import com.lin.takeout.mapper.EmployeeMapper;
import com.lin.takeout.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;


@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    //分页查询所有员工/按姓名查询
    @Override
    public Result<Page> getEmployeePage(int page, int pageSize, String name) {

        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //当传入查询的员工姓名时按姓名查询
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeMapper.selectPage(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }

    //登录验证
    @Override
    public Result<Employee> login(Employee employee) {

        //数据库里存储的时md5加密后的密码，因此也要md5加密密码后查询
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
        Employee relEmployee = employeeMapper.selectEmployeeByUserName(employee);

        //登录成功
        if (relEmployee.getPassword().equals(employee.getPassword()) && relEmployee.getUsername().equals(employee.getUsername()) && relEmployee.getStatus() == 1)
            return Result.success(relEmployee);

        //账号已被封禁
        if (relEmployee.getStatus() == 0)
            return Result.error("账号已被封禁");

        return Result.error("登录失败");
    }

    //查询指定id员工
    @Override
    public Result<Employee> getEmployeeById(long id) {

        Employee employee = employeeMapper.selectById(id);

        if (employee != null)
            return Result.success(employee);

        return Result.error("未查询到该员工");
    }

    //修改员工信息
    @Override
    @Transactional
    public Result<String> updateEmployeeInfo(Employee employee) {

        long id = GetIdByThreadLocal.getId();
        if (employee.getName() == null){

            if (employeeMapper.updateEmployeeStatusById(employee) != 0)
                return Result.success("修改成功");
            else
                return Result.error("修改失败");

        }else {

            if (employeeMapper.updateById(employee) != 0)
                return Result.success("修改成功");
            else
                return Result.error("修改失败");

        }
    }

    //添加员工信息
    @Transactional
    public Result saveEmployee(Employee employee){

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setStatus(1);

        employeeMapper.insert(employee);

        return Result.success("插入成功");
    }
}
