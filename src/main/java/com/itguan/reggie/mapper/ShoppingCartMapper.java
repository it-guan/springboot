package com.itguan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itguan.reggie.controller.ShoppingCartController;
import com.itguan.reggie.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
