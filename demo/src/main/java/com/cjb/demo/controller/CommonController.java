package com.cjb.demo.controller;

import com.cjb.demo.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;


    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        String originName = file.getOriginalFilename();
        String suffix = originName.substring(originName.lastIndexOf("."));  //文件后缀

        String fileName = UUID.randomUUID().toString() + suffix;//使用UUID生成文件名，防止上传文件名重复，造成覆盖

        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();//目录不存在,则创建目录
        }

        try {
            file.transferTo(new File(basePath + fileName));  //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName); //返回文件名，用于写入菜品数据表的图片字段
    }

    @GetMapping("/download") //文件下载
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));  //通过输入流读取文件内容

            ServletOutputStream outputStream = response.getOutputStream();//通过输出流把文件写回浏览器，在浏览器显示图片


            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];  //byte数组，用来存放输入流中的文件内容
            int len = 0; //把输入流内容（上传的图片内容）写入bytes数组中
            while((len = fileInputStream.read(bytes)) != -1){  //当输入流最后一个字节读入数组时，.read(bytes)返回-1，此时输入流已全部读完（源代码解释道）
                outputStream.write(bytes,0,len); //往输出流中写，写bytes数组长度
                outputStream.flush();  //刷新一下

            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
