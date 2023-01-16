package com.itguan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itguan.reggie.dto.SetmealDto;
import com.itguan.reggie.mapper.SetmealMapper;
import com.itguan.reggie.pojo.Setmeal;
import com.itguan.reggie.pojo.SetmealDish;
import com.itguan.reggie.service.SetmealDishService;
import com.itguan.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetmealDishService setmealDishService;



//    操作了多张表 开启事务
    @Transactional
    public void saveWithDishes(SetmealDto setmealDto){
//        先将setmeal保存
        this.save(setmealDto);

//        再根据回填的id将dishes里的setmealID填上
        setmealDto.getSetmealDishes().stream().map((item) ->{

            item.setSetmealId(setmealDto.getId());


            return item;

        }).collect(Collectors.toList());

//      将setmealDishes保存起来
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }

    @Transactional
    @Override
    public SetmealDto getSetmealWithDishes(Long setmealId) {

        SetmealDto setmealDto = new SetmealDto();
//        查询到对应的套餐信息
        Setmeal setmeal = this.getById(setmealId);

//        将套餐信息复制到dto
        BeanUtils.copyProperties(setmeal,setmealDto);
//        将套餐包含菜品信息填写到dto
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<SetmealDish>().eq("setmeal_id", setmealId);
        List<SetmealDish> setmealDishList = setmealDishService.list(setmealDishQueryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);

        return setmealDto;

    }

    @Override
    public void updateWithDishes(SetmealDto setmealDto) {
        this.updateById(setmealDto);

//        把老的删除
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<SetmealDish>().eq("setmeal_id", setmealDto.getId());
        setmealDishService.remove(setmealDishQueryWrapper);

//        把新的存上
        setmealDto.getSetmealDishes().stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }

    @Override
    public void removeWithDishes(Long setmealId) {
        this.removeById(setmealId);
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<SetmealDish>().eq("setmeal_id", setmealId);
        setmealDishService.remove(setmealDishQueryWrapper);
    }


}
