package com.itguan.reggie.common;

import ch.qos.logback.classic.pattern.LineOfCallerConverter;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getLoggerId());
        metaObject.setValue("updateUser",BaseContext.getLoggerId());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        System.out.println("------当前线程id: " + Thread.currentThread().getId());
//        System.out.println("获取到的loggerId:" + BaseContext.getLoggerId());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getLoggerId());

    }
}
