package com.lin.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.takeout.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    Employee selectEmployeeByUserName(Employee employee);

    int updateEmployeeStatusById(Employee employee);
}
