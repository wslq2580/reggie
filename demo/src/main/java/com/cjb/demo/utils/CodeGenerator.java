package com.cjb.demo.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/*
* mybatis-plus 代码生成器（新）
* @since 2023-05-17
* */
public class CodeGenerator {
    public static void main(String[] args) {
        generate();
    }

    private static void  generate(){
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/stroge?characterEncoding=UTF-8&serverTimezone=GMT%2b8&useSSL=false",
                        "root", "123456")  //数据库连接配置
                .globalConfig(builder -> {
                    builder.author("cjb") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\idea\\demo\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.cjb.demo") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\idea\\demo\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_user") // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                })
                //.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
