package com.itguan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itguan.reggie.common.R;
import com.itguan.reggie.pojo.Employee;
import com.itguan.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.xml.soap.SAAJResult;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @ResponseBody
    @PostMapping("/login")
    //密码加密 查询用户名 比对密码 查看状态 登陆成功
    public R<Employee> login(HttpSession session,@RequestBody Employee employee){
        String password = employee.getPassword();
        String username = employee.getUsername();

        //将密码进行md5加密
        password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<Employee>().eq("username", username);
        Employee logger = employeeService.getOne(employeeQueryWrapper);
        if (logger == null) return R.error("账号不存在，请检查账号信息");

//        System.out.println("logger password : " + logger.getPassword().toString() + "login password : " + password.toString());

        if (!logger.getPassword().equals(password)) return R.error("密码错误，请检查密码");

        if (logger.getStatus() == 0) return R.error("该账号已被禁用！");

        session.setAttribute("logger",logger);

        return R.success(logger);
    }

    @ResponseBody
    @PostMapping("/logout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("logger");
        return R.success("退出成功");
    }
    @ResponseBody
    @GetMapping("/page")
    public R<Page<Employee>> page(@RequestParam("page") Integer page,@RequestParam("pageSize") Integer pageSize,@RequestParam(value = "name",required = false) String name){
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
//        名字不为空 添加查询条件
        if (StringUtils.hasLength(name)) employeeQueryWrapper.like("name",name);

//        添加排序条件 按照更新时间进行排序
        employeeQueryWrapper.orderByDesc("update_time");

        Page<Employee> employeePage = new Page<>(page, pageSize);
        Page<Employee> employeePageInfo = employeeService.page(employeePage,employeeQueryWrapper);

//        System.out.println("查询到的员工数据：");
//        for (Employee employee : employeePageInfo.getRecords()) {
//            System.out.println("id = " + employee.getId() + ";;;;username = " + employee.getUsername() + ";;;;name = " + employee.getName()  + ";;;;sex = " + employee.getSex());
//        }

//        System.out.println("查询到的员工分页数据" + employeePageInfo.getRecords());
        return R.success(employeePageInfo);
    }


    @ResponseBody
    @PostMapping
    public R<String> save(HttpSession session,@RequestBody Employee employee){

//      设置初始密码并加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//      设置创建时间/更新时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

//      设置创建者id/更新操作者id
//        Employee logger = (Employee) session.getAttribute("logger");
//        employee.setCreateUser(logger.getId());
//        employee.setUpdateUser(logger.getId());

//        System.out.println("新增的的员工信息：" + employee.toString());

        boolean save = employeeService.save(employee);
        if (save) return R.success("保存成功");
        return R.error("失败");

    }

    @ResponseBody
    @PutMapping
    public R<String> update(HttpSession session,@RequestBody Employee employee){

//        System.out.println("------当前线程id: " + Thread.currentThread().getId());

//        System.out.println(employee.toString());

        Employee logger = (Employee) session.getAttribute("logger");
//        不能操作自己的账号
//        if (logger.getId() == employee.getId()) return R.error("英雄可以受委屈，但你不能踩自己的切尔西！！");

//        设置更新时间以及更新操作者
//        employee.setUpdateUser(logger.getId());
//        employee.setUpdateTime(LocalDateTime.now());

        boolean update = employeeService.updateById(employee);

//        System.out.println(update);

        if (!update) return R.error("修改失败");

        return R.success("修改成功");
    }

    @ResponseBody
    @GetMapping("/{id}")
    public R<Employee> queryEmployeeById(@PathVariable("id") Long id){
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("id",id);
        Employee employee = employeeService.getOne(employeeQueryWrapper);

        if (employee == null) return R.error("没有查询到对应的员工信息");

        return R.success(employee);
    }

}
