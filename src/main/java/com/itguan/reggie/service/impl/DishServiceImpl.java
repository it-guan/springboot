package com.itguan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itguan.reggie.dto.DishDto;
import com.itguan.reggie.mapper.DishMapper;
import com.itguan.reggie.pojo.Dish;
import com.itguan.reggie.pojo.DishFlavor;
import com.itguan.reggie.service.DishFlavorService;
import com.itguan.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;


    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
//        将菜品的信息保存到菜品表 DishDto是继承的Dish 所以直接当做参数传进去
        this.save(dishDto);

        Long id = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

//        将菜品的id保存到每一个flavor中 操作方式我也看不懂 但是使用增强for应该也可以实现（即for (T item : list）)
        flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

//        batch 批
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getDishWithFlavorById(Long dishId) {

        DishDto dishDto = new DishDto();

        Dish dish = this.getById(dishId);

        QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<DishFlavor>().eq("dish_id", dishId);

        List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorQueryWrapper);

        BeanUtils.copyProperties(dish,dishDto);

        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

//        因为dishFlavor表中的数据是多条存在的 在执行更新时可能有的信息会在前台被删除 所以可能会被忽略掉 所以就全部删掉重新添加


//        通过dish_id将之前该菜品存在的口味删除掉
        QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<DishFlavor>().eq("dish_id", dishDto.getId());
        dishFlavorService.remove(dishFlavorQueryWrapper);

//        将新的口味数据传进去
//            同上面新增的方法一样 传进来的flavor时没有dish_id的 需要再保存一下
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

}
