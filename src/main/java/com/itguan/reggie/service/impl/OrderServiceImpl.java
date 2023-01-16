package com.itguan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itguan.reggie.common.BaseContext;
import com.itguan.reggie.common.CustomException;
import com.itguan.reggie.pojo.*;
import com.itguan.reggie.mapper.OrderMapper;
import com.itguan.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    UserService userService;

    @Autowired
    AddressBookService addressBookService;
    @Override
    @Transactional
    public void submit(Orders orders) {

//        获取当前用户信息
        Long loggerId = BaseContext.getLoggerId();
        User logger = userService.getById(loggerId);
//        查询用户购物车数据
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id",loggerId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartQueryWrapper);
        if (shoppingCartList == null || shoppingCartList.size() == 0) throw new CustomException("购物车为空，暂时不能下单");

//        查询地址信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null) throw new CustomException("地址信息有误，暂时不能下单");
//        生成订单
        long orderId = IdWorker.getId();//生成唯一id
        AtomicInteger amount = new AtomicInteger(0);//用于计算总金额（不理解不懂不知道
//        通过购物车商品list生成orderDetail信息 同时计算总金额
        List<OrderDetail> orderDetailList =  shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(loggerId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(logger.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName()==null?"":addressBook.getProvinceName()) +
                (addressBook.getCityName() == null ? "" : addressBook.getCityName()) +
                (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);
//        为每一样商品生成订单明细
        orderDetailService.saveBatch(orderDetailList);
//        清空购物车
        shoppingCartService.remove(null);
    }
}