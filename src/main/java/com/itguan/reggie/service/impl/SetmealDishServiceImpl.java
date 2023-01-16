package com.itguan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itguan.reggie.mapper.SetmealDishMapper;
import com.itguan.reggie.mapper.SetmealMapper;
import com.itguan.reggie.pojo.Setmeal;
import com.itguan.reggie.pojo.SetmealDish;
import com.itguan.reggie.service.SetmealDishService;
import com.itguan.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
