package com.example.entity.vo.response;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicDetailVo {

    private Integer id;
    private String title;
    private String content;
    private Integer type;
    private Date createTime;

    private  User user;

    private Interact interact;

    private Long comments;//评论数量

    @Data
    @AllArgsConstructor
    public static class Interact {
        Boolean like;
        Boolean collect;
    }

    @Data
    public static class User{
        Integer id;
        String username;

        String avatar;

        String description;

        boolean gender;

        String qq;
        String wx;
        String phone;
        String email;

    }

}
