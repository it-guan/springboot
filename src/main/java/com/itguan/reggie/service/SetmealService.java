package com.itguan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itguan.reggie.dto.SetmealDto;
import com.itguan.reggie.pojo.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDishes(SetmealDto setmealDto);

    public SetmealDto getSetmealWithDishes(Long setmealId);

    public void updateWithDishes(SetmealDto setmealDto);

    public void removeWithDishes(Long setmealId);

}
