package com.nbmly.controller;

import com.nbmly.pojo.Result;
import com.nbmly.utils.AliOssUtill;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        //把文件内容存储到本地磁盘上
        String originalFilename = file.getOriginalFilename();
        //保证文件名字唯一，防止覆盖
        String filename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        //file.transferTo(new File("G:\\spring\\big-event\\files\\"+filename));
        String url = AliOssUtill.upLoadFile(filename, file.getInputStream());
        return Result.success(url);
    }
}
