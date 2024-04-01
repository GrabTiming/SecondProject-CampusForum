package com.example.utils;

import com.example.entity.RestBean;

import java.util.function.Supplier;

public class ControllerUtils {

    public static <T> RestBean<T> messageHandle(Supplier<String> action)
    {
        String message =action.get();
        if(message==null) return RestBean.success();
        else return RestBean.failure(400,message);
    }

}
