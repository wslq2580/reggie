package com.cjb.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjb.demo.entity.DishFlavor;
import com.cjb.demo.mapper.DishFlavorMapper;
import com.cjb.demo.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
