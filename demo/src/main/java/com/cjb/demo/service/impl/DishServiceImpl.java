package com.cjb.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjb.demo.DTO.DishDTO;
import com.cjb.demo.common.R;
import com.cjb.demo.entity.Dish;
import com.cjb.demo.entity.DishFlavor;
import com.cjb.demo.mapper.DishMapper;
import com.cjb.demo.service.DishFlavorService;
import com.cjb.demo.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override //新增菜品同时保存对应的口味数据
    public void saveWithFlavor(DishDTO dishDTO) {
        this.save(dishDTO);//保存菜品的信息到菜品表

        Long dishId = dishDTO.getId(); //菜品的ID
        List<DishFlavor> flavor = dishDTO.getFlavors(); //口味数组，但此时每个口味没有对应菜品的ID
        flavor.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList()); //遍历口味数组，给每个口味设置菜品ID，再组成新的数组

        dishFlavorService.saveBatch(flavor);//保存口味数据到口味表dish_flavor

        Long categoryId = dishDTO.getCategoryId();
        Set keys = redisTemplate.keys("dish_"+categoryId + "_1");//清理该分类菜品redis数据
        redisTemplate.delete(keys);
    }

    @Override //根据id查询菜品信息和口味信息
    public DishDTO getByIdWithFlavor(Long id) {

        DishDTO dishDTO = new DishDTO();


        //1、查询菜品基本信息，从dish表查
        Dish dish = this.getById(id);

        BeanUtils.copyProperties(dish,dishDTO);


        //2、查询菜品对应的口味信息，从dish_flavor表
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDTO.setFlavors(flavors);

        return dishDTO;
    }

    @Transactional
    @Override //更新菜品信息和对应口味
    public void updateWithFlavor(DishDTO dishDTO) {
          //1、从dish表更新菜品信息
        this.updateById(dishDTO);

          //2、（1）从dish_flavor表删除对应口味信息
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDTO.getId());
        dishFlavorService.remove(lambdaQueryWrapper);

        //2、（2）从dish_flavor表添加对应口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(dishDTO.getId());
            return item;
        }).collect(Collectors.toList()); //遍历口味数组，给每个口味设置菜品ID，再组成新的数组
        dishFlavorService.saveBatch(flavors);

        Long categoryId = dishDTO.getCategoryId();
        Set keys = redisTemplate.keys("dish_"+categoryId + "_1");//清理该分类菜品redis数据
        redisTemplate.delete(keys);
    }
}
