package com.lin.takeout.controller;

import com.lin.takeout.common.Result;
import com.lin.takeout.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/common")
public class CommonContorller {

    @Autowired
    CommonService commonService;

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws Exception {
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("image/jpeg");
        commonService.getImg(name,outputStream);
    }

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        return commonService.uploadImg(file);
    }
}
