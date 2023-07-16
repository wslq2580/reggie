package com.cjb.demo.DTO;

import com.cjb.demo.entity.Dish;
import com.cjb.demo.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDTO extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>(); //前端传来的口味列表

    private String categoryName;
    private String copies;
}
