package com.itguan.reggie.common;

public class BaseContext {
//    为当前线程声明一个threadLocal
    public static ThreadLocal<Long>  threadLocal = new ThreadLocal<>();

    public static void setLoggerId(Long loggerId) {
        threadLocal.set(loggerId);
    }

    public static Long getLoggerId(){
        return threadLocal.get();
    }

}
