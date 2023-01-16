package com.itguan.reggie.config;

import com.itguan.reggie.common.JacksonObjectMapper;
import com.itguan.reggie.interceptor.LoginInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
@ComponentScan("com.itguan.reggie")
public class WebMvcConfig extends WebMvcConfigurationSupport {

//    将 "backend" "front" 两个路径设置为静态资源路径 这样就可以直接访问
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

//    添加登录拦截器
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        LoginInterceptor loginInterceptor = new LoginInterceptor();


        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(
                "/backend/js/**",
                "/front/js/**",

                "/backend/images/**",
                "/front/images/**",

                "/backend/plugins/**",

                "/backend/styles/**",
                "/front/styles/**",

                "/backend/api/**",
                "/front/api/**",

                "/employee/login",
                "/user/login",

                "/backend/page/login/login.html",
                "/front/page/login.html",

                "/front/fonts/**",

                "/user/sendMsg");

    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,mappingJackson2HttpMessageConverter);
    }
}
