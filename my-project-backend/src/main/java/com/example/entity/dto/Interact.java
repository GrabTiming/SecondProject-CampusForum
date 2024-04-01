package com.example.entity.dto;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 互动操作 点赞 或者 收藏
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Interact {

    //所属文章
    private Integer tid;

    //操作者
    private Integer uid;

    //操作的时间
    private Date time;

    //使点赞还是收藏
    private String type;


    //存进Redis的用于识别这个操作的key
    public String getKey()
    {
        return tid+":"+uid;
    }


    public static Interact parseInteract(String str,String type)
    {
        String[] keys = str.split(":");

        return new Interact(Integer.parseInt(keys[0]),Integer.parseInt(keys[1]),new Date(),type);
    }


}
