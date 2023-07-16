package com.cjb.demo.common;


/*
* 基于ThreadLocal封装的工具类，保存当前登录用户的id
* */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setThreadLocal(Long id){
        threadLocal.set(id);
    }

    public static Long getThreadLocal(){
        return threadLocal.get();
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
}
