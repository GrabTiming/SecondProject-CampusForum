package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 需要的数据：
 * 用户信息：头像 名称
 * 评论内容
 */
public class CommentVo {

    private Integer id;
    private String content;
    private Date createTime;
    String quote;
    User user;

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
