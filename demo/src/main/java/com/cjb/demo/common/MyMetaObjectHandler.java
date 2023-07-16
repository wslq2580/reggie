package com.cjb.demo.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/*
* 元数据对象处理器
* */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("插入时，公共字段自动填充");
        log.info(metaObject.toString());

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getThreadLocal());  //BaseContext.getThreadLocal()，获取在登录拦截器中保存的线程变量保存的登录人id
        metaObject.setValue("updateUser", BaseContext.getThreadLocal());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("插入或更新时，公共字段自动填充");
        log.info(metaObject.toString());

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getThreadLocal());
    }
}
