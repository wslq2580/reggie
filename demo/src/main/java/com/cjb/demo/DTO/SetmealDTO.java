package com.cjb.demo.DTO;


import com.cjb.demo.entity.Setmeal;
import com.cjb.demo.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDTO extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
