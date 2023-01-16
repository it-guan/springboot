package com.itguan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itguan.reggie.dto.DishDto;
import com.itguan.reggie.pojo.Dish;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getDishWithFlavorById(Long dishId);

    public void updateWithFlavor(DishDto dishDto);

}
