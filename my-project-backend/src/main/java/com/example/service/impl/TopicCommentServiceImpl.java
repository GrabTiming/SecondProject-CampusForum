package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.TopicCommentMapper;
import com.example.entity.dto.TopicComment;
import com.example.service.TopicCommentService;
import org.springframework.stereotype.Service;

/**
 * (TopicComment)表服务实现类
 *
 * @author makejava
 * @since 2024-04-01 14:39:20
 */
@Service("topicCommentService")
public class TopicCommentServiceImpl extends ServiceImpl<TopicCommentMapper, TopicComment> implements TopicCommentService {

}

