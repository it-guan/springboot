package com.itguan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itguan.reggie.common.BaseContext;
import com.itguan.reggie.common.R;
import com.itguan.reggie.pojo.Orders;
import com.itguan.reggie.service.OrderService;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        orderService.submit(orders);

        return R.success("完成订单！");
    }

    @GetMapping("/userPage")
    public R<Page> page(@RequestParam("page") Integer page,@RequestParam("pageSize")Integer pageSize) {

        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.eq("user_id", BaseContext.getLoggerId());
        ordersQueryWrapper.orderByDesc("order_time");
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        orderService.page(ordersPage,ordersQueryWrapper);

        return R.success(ordersPage);
    }
}
