package com.itguan.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itguan.reggie.Utils.SMSUtils;
import com.itguan.reggie.Utils.ValidateCodeUtils;
import com.itguan.reggie.common.BaseContext;
import com.itguan.reggie.common.R;
import com.itguan.reggie.pojo.User;
import com.itguan.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.swing.plaf.IconUIResource;
import java.util.Map;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public R<String> login(HttpSession session,@RequestBody Map map) {

        System.out.println(map);

        String phone = (String) map.get("phone");

        String code = (String) map.get("code");
//        log.info(phone);

        if (StringUtils.hasLength(code)) {
            String realCode = (String) session.getAttribute(phone);
            System.out.println("code:" + code + ",realCode" + realCode);
            if (code.equals(realCode)) {
//                System.out.println("对比一致");
//                如果用户不存在 就创建一个新账号

                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("phone",phone);
                User user = userService.getOne(userQueryWrapper);
                if (user == null) {
                    user = new User();
                    user.setPhone(phone);
                    userService.save(user);
                }

//                将用户的id保存到baseContext
//                BaseContext.setLoggerId(user.getId());

                session.setAttribute("userLogger",user.getId());
                return R.success("ok");
            }
        }

        return R.error("错误");
    }

    @PostMapping("/sendMsg")
    public R<String> sendMsg(HttpSession session, @RequestBody Map map){

        System.out.println("访问到了sendMsg");

//        获取手机号
        String phone = (String) map.get("phone");

        if (StringUtils.hasLength(phone)) {

//        生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            System.out.println("生成的验证码：" + code);
//        发送短信
//            SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);
//        将验证码保存到session
            session.setAttribute(phone,code);

            return R.success("短信发送成功！");
        }

        return R.error("短信发送失败！");
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("userLogger");
        return R.success("退出成功！");
    }

}
