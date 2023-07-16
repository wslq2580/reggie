package com.cjb.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjb.demo.DTO.DishDTO;
import com.cjb.demo.entity.Dish;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDTO dishDTO); //新增菜品同时保存对应的口味数据
    public DishDTO getByIdWithFlavor(Long id); //根据id查询菜品信息和口味信息

    public void updateWithFlavor(DishDTO dishDTO); //更新菜品信息和对应口味
}
