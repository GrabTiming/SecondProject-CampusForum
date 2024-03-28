package com.example.entity.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("db_account_details")
public class AccountDetails implements BaseData  {

    @TableId(type = IdType.AUTO)
    private Integer id;
    //性别(1男2女)
    private int gender;
    //电话号码
    private String phone;
    //qq号
    private String qq;
    //微信号
    private String wx;
    //用户简介信息
    private String description;

}
