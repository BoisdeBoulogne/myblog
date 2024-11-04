package com.demo.myblog.controller;

import com.demo.myblog.entry.table.User;
import com.demo.myblog.mapper.UserMapper;
import com.demo.myblog.service.impl.MinioService;
import io.minio.GetObjectResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/file")
public class UploadFileController {
    @Autowired
    private MinioService minioService;
    @Resource
    UserMapper userMapper;

    @PostMapping
    public String uploadFile(MultipartFile file) throws Exception {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;

        minioService.putObject(file.getInputStream(),fileName,file.getContentType());
        return fileName;
    }


    @GetMapping("{fileName}")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) throws Exception  {
        // 设置响应类型
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        // 获取文件流
        GetObjectResponse objectResponse = minioService.getObject(fileName);
        // 将文件流输出到响应流
        IOUtils.copy(objectResponse, response.getOutputStream());
        // 结束
        response.flushBuffer();
        objectResponse.close();
    }

    // 根据文件名删除文件
    @DeleteMapping("{fileName}")
    public String remove(@PathVariable("fileName") String fileName) throws Exception  {
        minioService.removeObject(fileName);
        return "success";
    }

    @GetMapping("/test")
    public String test() throws Exception {
        User user = userMapper.selectById(1);
        System.out.println(user);
        return "success";
    }

}
