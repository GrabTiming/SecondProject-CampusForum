package com.example.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.entity.RestBean;
import com.example.entity.dto.Interact;
import com.example.entity.dto.TopicType;
import com.example.entity.vo.request.AddCommentVo;
import com.example.entity.vo.request.TopicCreateVo;
import com.example.entity.vo.request.TopicUpdateVo;
import com.example.entity.vo.response.CommentVo;
import com.example.entity.vo.response.TopicDetailVo;
import com.example.entity.vo.response.TopicPreviewVo;
import com.example.entity.vo.response.TopicTypeVo;
import com.example.service.TopicService;
import com.example.utils.Const;
import com.example.utils.ControllerUtils;
import com.google.gson.JsonObject;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Resource
    private TopicService topicService;

    /**
     * @return 获取所有文章类型
     */
    @GetMapping("/types")
    public RestBean<List<TopicTypeVo>> listType()
    {
        return RestBean.success(topicService.listTypes()
                .stream()
                .map(type->type.asViewObject(TopicTypeVo.class))
                .toList());
    }

    /**
     * 创建 帖子
     */
    @PostMapping("/create-topic")
    public RestBean<Void> topicCreate(@RequestBody @Valid TopicCreateVo topicCreateVo,
                                      @RequestAttribute(Const.ATTR_USER_ID) int id)
    {

       return ControllerUtils.messageHandle(()->topicService.createTopic(topicCreateVo,id));
    }

    @GetMapping("/list-topic")
    public  RestBean<List<TopicPreviewVo>> listTopic(@RequestParam @Min(0) int page,
                                                     @RequestParam @Min(0) int type)
    {
        return RestBean.success(topicService.listTopicByPage(page+1,type));
    }


    //点击文章预览 进入文章详情页面
    @GetMapping("/topic")
    public RestBean<TopicDetailVo> getTopic(@RequestParam @Min(0) int tid,
                                            @RequestAttribute(Const.ATTR_USER_ID) int uid)
    {
        return RestBean.success(topicService.getTopicDetailById(tid,uid));
    }

    //点赞收藏

    @GetMapping("/interact")
    public RestBean<Void> interact(@RequestParam @Min(0) int tid,
                                   @RequestParam @Pattern(regexp = "(like|collect)") String type,
                                   @RequestParam boolean state,
                                   @RequestAttribute(Const.ATTR_USER_ID) int id)
    {

        topicService.interact(new Interact(tid,id,new Date(),type),state);
        return RestBean.success();
    }


    @GetMapping("/collects")
    public RestBean<List<TopicPreviewVo>> listCollects(@RequestAttribute(Const.ATTR_USER_ID) int uid)
    {
        return RestBean.success(topicService.listTopicCollects(uid));
    }


    @PostMapping("/update-topic")
    public RestBean<Void> updateTopic(@Valid @RequestBody TopicUpdateVo vo,
                                      @RequestAttribute(Const.ATTR_USER_ID) int id)
    {
        return ControllerUtils.messageHandle(()->topicService.updateTopic(id,vo));
    }

    //添加评论
    @PostMapping("/add-comment")
    public RestBean<Void> addComment(@Valid @RequestBody AddCommentVo vo,
                                     @RequestAttribute(Const.ATTR_USER_ID) int uid)
    {
        return ControllerUtils.messageHandle(()->topicService.createComment(uid,vo));
    }


    @GetMapping("/comments")
    public RestBean<List<CommentVo>> comments(@RequestParam @Min(0) int tid,
                                              @RequestParam("page") @Min(0)  int pageNum)
    {
        return RestBean.success(topicService.getComments(tid,pageNum+1));
    }

    @PostMapping("/delete-comment")
    public RestBean<Void> deleteComment(@RequestParam @Min(0) int commentId,
                                        @RequestAttribute(Const.ATTR_USER_ID) int uid)
    {
        topicService.deleteComment(commentId,uid);
        return RestBean.success();
    }

}
