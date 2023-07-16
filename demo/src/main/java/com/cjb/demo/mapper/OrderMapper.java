package com.cjb.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjb.demo.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}