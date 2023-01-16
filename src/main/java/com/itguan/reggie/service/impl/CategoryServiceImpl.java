package com.itguan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itguan.reggie.mapper.CategoryMapper;
import com.itguan.reggie.mapper.EmployeeMapper;
import com.itguan.reggie.pojo.Category;
import com.itguan.reggie.pojo.Employee;
import com.itguan.reggie.service.CategoryService;
import com.itguan.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
