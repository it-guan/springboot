package com.itguan.reggie;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itguan.reggie.Utils.SMSUtils;
import com.itguan.reggie.Utils.ValidateCodeUtils;
import com.itguan.reggie.pojo.Employee;
import com.itguan.reggie.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootReggieApplicationTests {

    @Autowired
    EmployeeService employeeService;

    @Test
    void contextLoads() {

        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<Employee>().eq("username", "admin");
        Employee employee = employeeService.getOne(employeeQueryWrapper);
        System.out.println(employee);

    }

    @Test
    void codeTest(){
        //        生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        String s = Integer.toString(code);
        System.out.println(s);
//        发送短信
        SMSUtils.sendMessage("阿里云短信测试","SMS_154950909","18439566930",s);
    }

    @Test
    void gitTest(){
        System.out.println("git test");
    }

    @Test
    void branchTest(){
        System.out.println("branch test");
    }

}
