package com.cjb.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjb.demo.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cjb
 * @since 2023-07-10
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
