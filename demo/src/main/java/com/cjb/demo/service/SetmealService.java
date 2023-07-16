package com.cjb.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjb.demo.DTO.SetmealDTO;
import com.cjb.demo.entity.Setmeal;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDTO setmealDTO); // 保存套餐信息以及关联菜品信息

    public void removeWithDish(List<Long> ids); //删除套餐和对应的菜品，操作两张表
}
