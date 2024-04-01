package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.TopicComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * (TopicComment)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-01 14:39:19
 */
@Mapper
public interface TopicCommentMapper extends BaseMapper<TopicComment> {

}

