package com.example.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    //私有构造方法 不能new
    private BeanCopyUtils(){

    }

    public static <V> V copyBean(Object source,Class <V> clazz) {
        //创建目标对象
        V result = null;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(source, result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz)
    {
        //不用for循环，改用stream流进行转换
        return list.stream().
               map(o-> copyBean(o,clazz)).
               //将结果转换为集合
               collect(Collectors.toList());
    }

}
