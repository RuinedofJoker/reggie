package com.lin.takeout.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result<D> {

    private Integer code;
    private String msg;
    private D data;
    private Map map = new HashMap();

    public static <D> Result<D> success(D data){
        Result<D> result = new Result();
        result.code = 1;
        result.data = data;
        return result;
    }

    public static <D> Result<D> error(String msg){
        Result<D> result = new Result();
        result.code = 0;
        result.msg = msg;
        return result;
    }

    public Result<D> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
