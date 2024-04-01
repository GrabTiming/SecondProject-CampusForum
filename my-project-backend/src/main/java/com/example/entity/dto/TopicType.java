package com.example.entity.dto;


import java.io.Serializable;

import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 帖子类型
 *
 * @author makejava
 * @since 2024-03-29 15:59:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("db_topic_type")
public class TopicType  implements BaseData {
    
    @TableId
    private Integer id;

    private String name;

    private String description;

    private String color;
    
}
