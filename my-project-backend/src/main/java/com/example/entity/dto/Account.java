package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 数据库中的用户信息
 */
@Data
@TableName("db_account")
@AllArgsConstructor
public class Account implements BaseData {

    @TableId(type = IdType.AUTO)
    Integer id;
    //用户名
    String username;
    //密码
    String password;
    //邮箱
    String email;
    //后续处理的角色 还没用上
    String role;
    //注册时间
    Date registerTime;
    //头像
    String avatar;
}
