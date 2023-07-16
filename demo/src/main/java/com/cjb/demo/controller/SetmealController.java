package com.cjb.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjb.demo.DTO.SetmealDTO;
import com.cjb.demo.common.R;
import com.cjb.demo.entity.Category;
import com.cjb.demo.entity.Dish;
import com.cjb.demo.entity.Setmeal;
import com.cjb.demo.service.CategoryService;
import com.cjb.demo.service.SetmealDishService;
import com.cjb.demo.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping //新增套餐
    public R<String> save(@RequestBody SetmealDTO setmealDTO){
        setmealService.saveWithDish(setmealDTO);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page") //套餐分页查询
    public R<Page> page(int page, int pageSize, String name){
        Page<Setmeal> p = new Page<>(page,pageSize);
        Page<SetmealDTO> p2 = new Page<>(page,pageSize);

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null, Setmeal::getName,name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(p,lambdaQueryWrapper);

        List<Setmeal> records = p.getRecords();
        BeanUtils.copyProperties(p,p2,"records");
        //获取套餐分类的名称

        List<SetmealDTO> list2 = records.stream().map((item) ->{

            SetmealDTO setmealDTO = new SetmealDTO();
            BeanUtils.copyProperties(item,setmealDTO);

            Long setmealId = item.getCategoryId();
            Category category = categoryService.getById(setmealId);
            if(category != null){
                String categoryNameE = category.getName();
                setmealDTO.setCategoryName(categoryNameE);
            }
            return setmealDTO;
        }).collect(Collectors.toList());

        p2.setRecords(list2); //把新的records放进Page的records中
        return R.success(p2);
    }

    @DeleteMapping //删除套餐
    public R<String> delete(@RequestParam List<Long> ids){

        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Long categoryId){
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Setmeal::getCategoryId,categoryId);
        lambdaQueryWrapper.eq(Setmeal::getStatus,1); //查询起售状态的套餐
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
