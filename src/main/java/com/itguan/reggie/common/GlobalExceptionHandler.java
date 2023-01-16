package com.itguan.reggie.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException sqlEx){

        System.out.println("捕捉到的异常：" + sqlEx.getMessage());

//        处理同名
        if (sqlEx.getMessage().contains("Duplicate entry")) {

            String[] strings = sqlEx.getMessage().split(" ");
            String msg = strings[2];
            return R.error(msg + "已存在");
        }

        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
        return R.error(ex.getMessage());
    }

}
