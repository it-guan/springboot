package com.itguan.reggie.interceptor;

import com.alibaba.druid.support.json.JSONUtils;
import com.itguan.reggie.common.BaseContext;
import com.itguan.reggie.common.R;
import com.itguan.reggie.pojo.Employee;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("paths:");
        System.out.println(request.getServletPath());



//        System.out.println(request.getServletPath());


        Employee logger = (Employee) request.getSession().getAttribute("logger");
        Long userLogger = (Long) request.getSession().getAttribute("userLogger");
//        ThreadLocal<Long> threadLocal = new ThreadLocal<>();
//        System.out.println("------当前线程id: " + Thread.currentThread().getId());

        if (userLogger != null) {
            BaseContext.setLoggerId(userLogger);
            System.out.println("放行");

            return true;
        }

        if (logger != null) {

//            request.setAttribute("res", R.error("请先登录！"));
//            request.getRequestDispatcher("/backend/page/login/login.html").forward(request,response);
//            response.getWriter().write(JSONUtils.toJSONString(R.error("NOTLOGIN")));

            BaseContext.setLoggerId(logger.getId());
            System.out.println("放行");
            return true;
        }

        if (request.getServletPath().contains("front"))
            response.sendRedirect("/front/page/login.html");

        else {
            response.sendRedirect("/backend/page/login/login.html");
        }

        System.out.println("拦截");
        return false;
    }
}
