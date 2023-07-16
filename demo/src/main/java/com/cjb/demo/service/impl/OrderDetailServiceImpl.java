package com.cjb.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjb.demo.entity.OrderDetail;
import com.cjb.demo.mapper.OrderDetailMapper;
import com.cjb.demo.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}