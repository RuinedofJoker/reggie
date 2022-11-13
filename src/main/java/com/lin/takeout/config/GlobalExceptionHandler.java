package com.lin.takeout.config;

import com.lin.takeout.common.CustomException;
import com.lin.takeout.common.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = {Controller.class, RestController.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class,Exception.class})
    public Result catchExceptionHandler(CustomException ex){
        return Result.error(ex.getMessage());
    }
}
