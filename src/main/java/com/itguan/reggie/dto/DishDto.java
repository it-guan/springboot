package com.itguan.reggie.dto;

import com.itguan.reggie.pojo.Dish;
import com.itguan.reggie.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
