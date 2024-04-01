package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Interact;
import com.example.entity.dto.Topic;
import com.example.entity.dto.TopicType;
import com.example.entity.vo.request.AddCommentVo;
import com.example.entity.vo.request.TopicCreateVo;
import com.example.entity.vo.request.TopicUpdateVo;
import com.example.entity.vo.response.CommentVo;
import com.example.entity.vo.response.TopicDetailVo;
import com.example.entity.vo.response.TopicPreviewVo;

import java.util.List;

/**
 * (Topic)表服务接口
 *
 * @author makejava
 * @since 2024-03-29 16:14:40
 */
public interface TopicService extends IService<Topic> {

    List<TopicType> listTypes();

    String createTopic(TopicCreateVo topicCreateVo, int id);


    List<TopicPreviewVo> listTopicByPage(int page,int type);


    //查询用户 文章 的收藏列表
    List<TopicPreviewVo> listTopicCollects(int uid);

    TopicDetailVo getTopicDetailById(int tid,int uid);


    //点赞 收藏
    void interact(Interact interact, boolean state);


    //编辑帖子
    String updateTopic(int uid, TopicUpdateVo vo);

    //创建评论
    String createComment(int uid, AddCommentVo vo);

    //获取评论
    List<CommentVo> getComments(int tid,int pageNum);

    //删除评论
    void deleteComment(int tid, int uid);
}

