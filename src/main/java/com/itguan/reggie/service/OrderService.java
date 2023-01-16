package com.itguan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itguan.reggie.pojo.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);

    /**
     * 用户下单
     * @param orders
     */

}
