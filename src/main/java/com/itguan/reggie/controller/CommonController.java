package com.itguan.reggie.controller;

import com.itguan.reggie.common.R;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

//    从yml配置文件中获取设置好的路径
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        System.out.println(file.toString());

//        获取原始文件名
        String originalFilename = file.getOriginalFilename();
//        获取文件名后缀                   截取index后的字符串（获取"."所在的下标）
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

//        使用UUID生成新的文件名，防止文件重复造成覆盖 加上截取到的后缀
        String fileName = UUID.randomUUID().toString() + suffix;

//        创建一个目录对象
        File dir = new File(basePath);
//        如果目录不存在 就创建一个
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    @GetMapping("download")
    public void download(String name, HttpServletResponse response){

        try {
            System.out.println(name);
//        通过输入流读取文件
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
//        通过输出流将文件写回到浏览器
//                获取response的输出流
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
