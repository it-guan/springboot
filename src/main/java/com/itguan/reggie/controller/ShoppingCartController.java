package com.itguan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itguan.reggie.common.BaseContext;
import com.itguan.reggie.common.R;
import com.itguan.reggie.pojo.ShoppingCart;
import com.itguan.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @RequestMapping("/list")
    public R<List> list(){
        Long userLogger = BaseContext.getLoggerId();
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
//        查询条件要加上当前登录者的id
        shoppingCartQueryWrapper.eq("user_id",BaseContext.getLoggerId());
        shoppingCartQueryWrapper.eq("user_id",userLogger);
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartQueryWrapper);
        return R.success(list);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

//        如果要加的菜品不在购物车中 就添加一个
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
//        查询条件要加上当前登录者的id
        shoppingCartQueryWrapper.eq("user_id",BaseContext.getLoggerId());
        if (shoppingCart.getDishId() != null) shoppingCartQueryWrapper.eq("dish_id",shoppingCart.getDishId());
        if (shoppingCart.getSetmealId() != null) shoppingCartQueryWrapper.eq("setmeal_id",shoppingCart.getSetmealId());
        ShoppingCart one = shoppingCartService.getOne(shoppingCartQueryWrapper);
        if (one == null) {
            shoppingCart.setUserId(BaseContext.getLoggerId());

            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCart.setNumber(1);

            shoppingCartService.save(shoppingCart);
            return R.success(shoppingCart);
        }
//        如果在购物车中 就讲数量加一
        Integer number = one.getNumber();
        number = number + 1;
        one.setNumber(number);
        shoppingCartService.updateById(one);


        return R.success(one);
    }

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){

//        如果数量那个大于1 就减一
//        如果数量等于1 那么就直接删掉


        UpdateWrapper<ShoppingCart> shoppingCartQueryWrapper = new UpdateWrapper<>();
//        查询条件要加上当前登录者的id
        shoppingCartQueryWrapper.eq("user_id",BaseContext.getLoggerId());
        if (shoppingCart.getDishId() != null) shoppingCartQueryWrapper.eq("dish_id",shoppingCart.getDishId());
        if (shoppingCart.getSetmealId() != null) shoppingCartQueryWrapper.eq("setmeal_id",shoppingCart.getSetmealId());
        ShoppingCart one = shoppingCartService.getOne(shoppingCartQueryWrapper);
        Integer number = one.getNumber();
        if (number > 1) {
            one.setNumber(number - 1);
            shoppingCartService.updateById(one);
            return R.success("减少成功！");
        }
        if (number == 1) {
            shoppingCartService.removeById(one);
            return R.success("减少成功！");
        }
        return R.error("减少失败！");
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
//        删除当前账号下的购物车的所有信息
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id",BaseContext.getLoggerId());
        shoppingCartService.remove(shoppingCartQueryWrapper);
        return R.success("清空数据！");
    }

}
