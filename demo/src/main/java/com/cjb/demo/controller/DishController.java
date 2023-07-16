package com.cjb.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjb.demo.DTO.DishDTO;
import com.cjb.demo.common.R;
import com.cjb.demo.entity.Category;
import com.cjb.demo.entity.Dish;
import com.cjb.demo.entity.DishFlavor;
import com.cjb.demo.service.CategoryService;
import com.cjb.demo.service.DishFlavorService;
import com.cjb.demo.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping  //新增菜品
    public R<String> save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page") //菜品列表分页查询(难)
    public R<Page> page(int page, int pageSize,String name){
        Page<Dish> p = new Page<>(page,pageSize);//构造分页构造器
        Page<DishDTO> p2 = new Page<>(page,pageSize);//构造分页构造器

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null, Dish::getName,name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(p,lambdaQueryWrapper);

        BeanUtils.copyProperties(p,p2,"records");//对象拷贝

        List<Dish> records = p.getRecords();

        List<DishDTO> list = records.stream().map((item) -> {
            DishDTO dishDTO = new DishDTO();

            BeanUtils.copyProperties(item,dishDTO);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null) {
                String categoryName = category.getName();
                dishDTO.setCategoryName(categoryName);
            }
            return dishDTO;
        }).collect(Collectors.toList());

        p2.setRecords(list);
        return R.success(p2);
    }

    @GetMapping("/{id}")
    public R<DishDTO> edit(@PathVariable Long id){
        DishDTO dishDTO = dishService.getByIdWithFlavor(id);

        return R.success(dishDTO);
    }

    @PutMapping //修改菜品
    public R<String> update(@RequestBody DishDTO dishDTO){
        dishService.updateWithFlavor(dishDTO);


        return R.success("修改菜品成功");
    }

//    @GetMapping("/list")  //根据条件查询对应的菜品数据
//    public R<List<DishDTO>> list(Dish dish){
//        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId,dish.getCategoryId());
//        lambdaQueryWrapper.eq(Dish::getStatus,1); //查询起售状态的菜品
//        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(lambdaQueryWrapper);
//
//        List<DishDTO> list2 = list.stream().map((item) -> {
//            DishDTO dishDTO = new DishDTO();
//            Long id = item.getId(); // 当前菜品ID
//            BeanUtils.copyProperties(item,dishDTO);
//            Long categoryId = item.getCategoryId();
//            Category category = categoryService.getById(categoryId);
//            if(category != null) {
//                String categoryName = category.getName();
//                dishDTO.setCategoryName(categoryName);
//            }
//            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
//            lambdaQueryWrapper1.eq(DishFlavor::getDishId,id);
//            List<DishFlavor> list1 = dishFlavorService.list(lambdaQueryWrapper1);
//            dishDTO.setFlavors(list1);
//            return dishDTO;
//        }).collect(Collectors.toList());
//
//        return R.success(list2);
//    }

    @GetMapping("/list")  //根据条件查询redis中对应的菜品数据，如果没有查询到，则去数据库查
    public R<List<DishDTO>> list(Dish dish){
        List<DishDTO> dishDTOList = null;

        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus(); //根据不同的分类，动态构造key

        //查询redis
        dishDTOList= (List<DishDTO>) redisTemplate.opsForValue().get(key);

        if(!CollectionUtils.isEmpty(dishDTOList)){
            return R.success(dishDTOList);//存在，直接返回
        }
        else{
                //不存在，则查询数据库
            LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId,dish.getCategoryId());
            lambdaQueryWrapper.eq(Dish::getStatus,1); //查询起售状态的菜品
            lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
            List<Dish> list = dishService.list(lambdaQueryWrapper);

            dishDTOList = list.stream().map((item) -> {
                DishDTO dishDTO = new DishDTO();
                Long id = item.getId(); // 当前菜品ID
                BeanUtils.copyProperties(item,dishDTO);
                Long categoryId = item.getCategoryId();
                Category category = categoryService.getById(categoryId);
                if(category != null) {
                    String categoryName = category.getName();
                    dishDTO.setCategoryName(categoryName);
                }
                LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(DishFlavor::getDishId,id);
                List<DishFlavor> list1 = dishFlavorService.list(lambdaQueryWrapper1);
                dishDTO.setFlavors(list1);
                return dishDTO;
            }).collect(Collectors.toList());

            redisTemplate.opsForValue().set(key,dishDTOList);//并将查询到的数据存入redis
            return R.success(dishDTOList);
        }

    }
}
