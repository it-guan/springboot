package com.itguan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itguan.reggie.common.CustomException;
import com.itguan.reggie.common.R;
import com.itguan.reggie.pojo.Category;
import com.itguan.reggie.pojo.Dish;
import com.itguan.reggie.pojo.Setmeal;
import com.itguan.reggie.service.CategoryService;
import com.itguan.reggie.service.DishService;
import com.itguan.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setMealService;


    @GetMapping("/page")
    public R<Page<Category>> page(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize){

        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.orderByDesc("update_time");

        Page<Category> categoryPage = new Page<>(page,pageSize);
        Page<Category> categoryPageInfo = categoryService.page(categoryPage,categoryQueryWrapper);

        return R.success(categoryPageInfo);
    }


    @PostMapping
    public R<String> save(@RequestBody Category category){

        boolean save = categoryService.save(category);
        if (!save) return R.error("添加失败！");

        return R.success("添加成功！");
    }


    @PutMapping
    public R<String> update(@RequestBody Category category){

        boolean update = categoryService.updateById(category);
        if (!update) return R.error("更新失败！");

        return R.success("修改成功！");
    }


    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long[] ids) {

        for (Long id : ids) {
            if (setMealService.list(new QueryWrapper<Setmeal>().eq("category_id",id)) != null )
                throw new CustomException("该分类下有绑定的套餐，暂时无法删除！");
            if (dishService.list(new QueryWrapper<Dish>().eq("category_id",id)) != null)
                throw new CustomException("该分类下有绑定的菜品，暂时无法删除！");
//            if (setMealService.list(new QueryWrapper<Setmeal>().eq("category_id",id)) != null || dishService.list(new QueryWrapper<Dish>().eq("category_id",id)) != null)
//                return R.error("该分类下有绑定的菜品，暂时无法删除！");
        }

        return R.success("删除成功！");
    }

    @GetMapping("/list")
    public R<List> list(Category category){

        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<Category>().eq(category.getType() != null, "type", category.getType());

        List<Category> categoryList = categoryService.list(categoryQueryWrapper);

        return R.success(categoryList);
    }

}
