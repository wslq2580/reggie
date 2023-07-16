package com.cjb.demo.service.impl;

import com.cjb.demo.entity.Employee;
import com.cjb.demo.mapper.EmployeeMapper;
import com.cjb.demo.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cjb
 * @since 2023-07-10
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
