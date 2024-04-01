package com.example.entity.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class TopicTypeVo {

    private Integer id;

    String name;

    String description;

    String color;

}
