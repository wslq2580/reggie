package com.cjb.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjb.demo.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
