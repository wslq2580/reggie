package com.cjb.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjb.demo.entity.ShoppingCart;
import com.cjb.demo.mapper.ShoppingCartMapper;
import com.cjb.demo.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
