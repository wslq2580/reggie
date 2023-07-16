package com.cjb.demo.common;

/*
* 异常全局处理
* */
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Conditional.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class) //捕获新增员工时用户名重复的异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){

        if(ex.getMessage().contains("Duplicate entry")){
            String[] exs = ex.getMessage().split(" ");
            String username = exs[2];
            return R.error("新增失败,用户名"+username+"重复");
        }
        else{
            return R.error("未知错误");
        }
    }

    @ExceptionHandler(CustomException.class) //捕获删除分类时，该分类已故演练其他菜品或套餐的异常
    public R<String> exceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }
}

