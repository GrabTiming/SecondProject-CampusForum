package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.TopicType;
import org.apache.ibatis.annotations.Mapper;

/**
 * (TopicType)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-29 16:00:57
 */
@Mapper
public interface TopicTypeMapper extends BaseMapper<TopicType> {

}

