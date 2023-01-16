package com.itguan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itguan.reggie.common.R;
import com.itguan.reggie.dto.DishDto;
import com.itguan.reggie.pojo.Category;
import com.itguan.reggie.pojo.Dish;
import com.itguan.reggie.pojo.DishFlavor;
import com.itguan.reggie.service.CategoryService;
import com.itguan.reggie.service.DishFlavorService;
import com.itguan.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {


    @Autowired
    CategoryService categoryService;
    @Autowired
    DishService dishService;

    @Autowired
    DishFlavorService dishFlavorService;

    @GetMapping("/page")
    public R<Page> page(@RequestParam("page")Integer page,@RequestParam("pageSize")Integer pageSize,@RequestParam(value = "name",required = false) String name){

        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();

        if (StringUtils.hasLength(name))
            dishQueryWrapper.like("name",name);

        dishQueryWrapper.orderByDesc("update_time");

        Page<Dish> dishPage = new Page<>(page,pageSize);

        Page<Dish> dishPageInfo = dishService.page(dishPage, dishQueryWrapper);



//        前端要获取的是分类名称 但是dish里存的是分类ID 所以要将获取到的信息进行处理
//          创建一个DishDto的分页信息
        Page<DishDto> dishDtoPageInfo = new Page<>();
//          将Dish分页信息中 records以外的数据保存到DishDto中
        BeanUtils.copyProperties(dishPageInfo,dishDtoPageInfo,"records");
//          对Dish的records进行处理 即将其中的数据都保存到DishDto的records中 再利用categoryId给categoryName赋值
        List<DishDto> dishDtoList = dishPageInfo.getRecords().stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Category category = categoryService.getById(dishDto.getCategoryId());

            dishDto.setCategoryName(category.getName());

            return dishDto;
        }).collect(Collectors.toList());

//        将records保存到DishDPageInfo中
        dishDtoPageInfo.setRecords(dishDtoList);
//          最后返回DishDtoPageInfo

        return R.success(dishDtoPageInfo);
    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

//        boolean save = dishService.save(dish);


        System.out.println("添加的菜品的所有信息" + dishDto);

        dishService.saveWithFlavor(dishDto);

//        System.out.println("添加的菜品信息：" + dish);
//        if (!save) return R.error("添加失败");
//        System.out.println("添加菜品的口味：" + flavors);
        return R.success("添加成功！");


    }

    @GetMapping("/{id}")
    public R<DishDto> dishInfo(@PathVariable("id") Long id){

        DishDto dishDto = dishService.getDishWithFlavorById(id);

//        System.out.println("编辑菜品-->查询到的菜品信息：" + dishDto);

        if (dishDto == null) return R.error("查询不到！");

        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

//        要执行更新的信息
        System.out.println("要执行更新的信息" + dishDto);

        dishService.updateWithFlavor(dishDto);

        return R.success("更新成功！");
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable("status") Integer status,@RequestParam("ids") Long[] ids) {

        for (Long id : ids) {
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }

        return R.success("修改成功！");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long[] ids) {

        dishService.removeBatchByIds(Arrays.asList(ids));

        return R.success("删除成功！");
    }

    @GetMapping("/list")
    public R<List> list(@RequestParam("categoryId") Long categoryId) {

//        根据分类id查询菜品
        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<Dish>().eq("category_id", categoryId);
//        只查询正在出售的
        dishQueryWrapper.eq("status",1);

        List<Dish> dishList = dishService.list(dishQueryWrapper);

        List<DishDto> dishDtoList = null;

        dishDtoList =  dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();

//            复制数据
            BeanUtils.copyProperties(item,dishDto);

//            添加口味信息
            QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
            dishFlavorQueryWrapper.eq("dish_id",item.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorQueryWrapper);
            dishDto.setFlavors(dishFlavorList);

//            添加分类的名称
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) dishDto.setCategoryName(category.getName());

            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

}
