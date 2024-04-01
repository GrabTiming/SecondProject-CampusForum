package com.example.entity.dto;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Topic)表
 * 帖子
 * @author makejava
 * @since 2024-03-29 16:14:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("db_topic")
public class Topic  {
    
    @TableId(type = IdType.AUTO)
    private Integer id;

    //标题
    private String title;
    //内容
    private String content;
    //作者id
    private Integer uid;
    //类型
    private Integer type;
    //创作时间
    private Date createTime;
    
}
