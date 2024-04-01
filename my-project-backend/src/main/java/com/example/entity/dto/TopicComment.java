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
 * 评论
 * 两层结构，只显示该评论与回复评论(如果有的话)
 * @author makejava
 * @since 2024-04-01 14:39:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("db_topic_comment")
public class TopicComment  {
    
    @TableId(type = IdType.AUTO)
    private Integer id;

    //发表评论的用户
    private Integer uid;
    //发表位置
    private Integer tid;
    //发表内容
    private String content;
    //发表时间
    private Date createTime;
    //上级评论的id
    private Integer quote;
    
}
