package com.lin.takeout.service;

import com.lin.takeout.common.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;

public interface CommonService {

    void getImg(String img, ServletOutputStream outputStream) throws Exception;

    Result<String> uploadImg(MultipartFile file) throws Exception;
}
