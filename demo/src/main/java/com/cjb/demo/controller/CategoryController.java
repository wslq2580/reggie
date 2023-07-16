package com.cjb.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjb.demo.common.R;
import com.cjb.demo.entity.Category;
import com.cjb.demo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping //新增分类
    public R<String> save(@RequestBody Category category){
        log.info("分类是：{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page") //分页查询
    public R<Page> page(int page,int pageSize){
        Page<Category> p = new Page<>(page,pageSize);  //分页构造器

        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort); //排序构造器

        categoryService.page(p,lambdaQueryWrapper);
        return R.success(p);
    }

    @DeleteMapping
    public R<String> delete(Long ids){

        categoryService.remove(ids);
        return R.success("分类删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    @GetMapping("/list")  //
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType() != null, Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
