package com.itguan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itguan.reggie.common.CustomException;
import com.itguan.reggie.common.R;
import com.itguan.reggie.dto.SetmealDto;
import com.itguan.reggie.pojo.Category;
import com.itguan.reggie.pojo.Dish;
import com.itguan.reggie.pojo.Setmeal;
import com.itguan.reggie.pojo.SetmealDish;
import com.itguan.reggie.service.CategoryService;
import com.itguan.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Autowired
    SetmealService setMealService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> page(@RequestParam("page") Integer page,@RequestParam("pageSize") Integer pageSize,@RequestParam(value = "name",required = false) String name) {

        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        setmealQueryWrapper.orderByDesc("update_time");
        if (StringUtils.hasLength(name)) setmealQueryWrapper.like("name",name);

        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage,setmealDtoPage);
        setMealService.page(setmealPage);
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> setmealDtoList =  records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Category category = categoryService.getById(item.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
//        System.out.println(setmealPage.getRecords());
        setmealDtoPage.setRecords(setmealDtoList);
        return R.success(setmealDtoPage);
    }

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        setMealService.saveWithDishes(setmealDto);

        return R.success("添加成功！");

    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable("status") Integer status,@RequestParam("ids")Long[] ids) {

        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setStatus(status);
            setMealService.updateById(setmeal);
        }

        return R.success("修改成功！");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> SetmealInfo(@PathVariable("id") Long id){

        SetmealDto setmealWithDishes = setMealService.getSetmealWithDishes(id);

        return R.success(setmealWithDishes);

    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){

        setMealService.updateWithDishes(setmealDto);

        return R.success("更新成功！");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long[] ids) {

        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<Setmeal>().eq("status", 1).in("id",ids);
        long count = setMealService.count(setmealQueryWrapper);
        if (count > 0)
            throw new  CustomException("有商品正在售卖中 请停售后再进行删除！");

        for (Long id : ids) {
            setMealService.removeWithDishes(id);
        }

        return R.success("删除成功！");
    }

//    如果是使用链接？键值对的形式进行传参 那么就不需要@RequestBody了 直接使用实体类接收即可
    @GetMapping("/list")
    public R<List> list(@RequestParam("categoryId") Long categoryId,@RequestParam("status") Integer status) {

        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setCategoryId(categoryId);

        List<Setmeal> setmealList = setMealService.list(new QueryWrapper<>(setmeal));

        List<SetmealDto> setmealDtoList = setmealList.stream().map((item) -> {
//            查询出带有菜品信息的dto
            SetmealDto setmealDto = setMealService.getSetmealWithDishes(item.getId());

//            添加所属分类名称
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) setmealDto.setCategoryName(category.getName());

            return setmealDto;
        }).collect(Collectors.toList());

        return R.success(setmealDtoList);
    }

    @GetMapping("/dish/{id}")
    public R<List> dish(@PathVariable("id") Integer id){

        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();


        return null;
    }

}
