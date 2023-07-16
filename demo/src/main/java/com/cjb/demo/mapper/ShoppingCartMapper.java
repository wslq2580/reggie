package com.cjb.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjb.demo.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
