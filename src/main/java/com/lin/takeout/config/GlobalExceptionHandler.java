package com.lin.takeout.config;

import com.lin.takeout.common.CustomException;
import com.lin.takeout.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice(annotations = {Controller.class, RestController.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class,Exception.class})
    public Result catchExceptionHandler(CustomException ex){

        log.info(ex.getMessage());
        return Result.error("未知错误");
    }
}
