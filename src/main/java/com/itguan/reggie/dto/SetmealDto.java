package com.itguan.reggie.dto;

import com.itguan.reggie.pojo.Setmeal;
import com.itguan.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
