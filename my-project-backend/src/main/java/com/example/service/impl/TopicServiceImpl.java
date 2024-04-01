package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.*;
import com.example.entity.vo.request.AddCommentVo;
import com.example.entity.vo.request.TopicCreateVo;
import com.example.entity.vo.request.TopicUpdateVo;
import com.example.entity.vo.response.CommentVo;
import com.example.entity.vo.response.TopicDetailVo;
import com.example.entity.vo.response.TopicPreviewVo;
import com.example.mapper.*;
import com.example.service.AccountDetailsService;
import com.example.service.TopicService;
import com.example.utils.CacheUtils;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * (Topic)表服务实现类
 *
 * @author makejava
 * @since 2024-03-29 16:14:45
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    private TopicTypeMapper topicTypeMapper;

    @Resource
    private  FlowUtils flowUtils;

    @Resource
    private CacheUtils cacheUtils;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private TopicCommentMapper commentMapper;

    @Resource
    private AccountPrivacyMapper accountPrivacyMapper;

    @Resource
    private AccountDetailsService accountDetailsService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private Set<Integer> types = null;
    @PostConstruct
    private void typesInit()
    {
        types = this.listTypes()
                .stream()
                .map(TopicType::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public List<TopicType> listTypes() {
        return topicTypeMapper.selectList(null);
    }

    @Override
    public String createTopic(TopicCreateVo topicCreateVo, int id) {
        if(!textLimitCheck(topicCreateVo.getContent(),20000))
            return "文章长度超出限制";
        if(!types.contains(topicCreateVo.getType()))
        {
            return "文章类型非法";
        }
        String key = Const.FORUM_TOPIC_CREATE_COUNTER+ id;
        if(!flowUtils.limitPeriodCheck(key,3,600))//10分钟内发三个帖子
        {
            return "操作频繁，请稍后重试";
        }
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicCreateVo,topic);
        topic.setContent(topicCreateVo.getContent().toJSONString());
        topic.setUid(id);
        topic.setCreateTime(new Date());

        if(this.save(topic)) {
            //将预览的缓存全部清空
            cacheUtils.deleteCache(Const.FORUM_TOPIC_PREVIEW+"*");
            return null;
        }
        else
        {
            return "内部错误，请联系管理员";
        }

    }

    @Override
    public List<TopicPreviewVo> listTopicByPage(int pageNum, int type) {
        String key = Const.FORUM_TOPIC_PREVIEW + pageNum + ":" +type;

        //看缓存是否有这个数据
        List<TopicPreviewVo> list = cacheUtils.takeListFromCache(key,TopicPreviewVo.class);
        if(list==null)
        {
            Page<Topic> page = new Page<>(pageNum,10);
            List<Topic> topics;
            if(type==0)
                baseMapper.selectPage(page, Wrappers.<Topic>query().orderByDesc("create_time"));
            else
                baseMapper.selectPage(page, Wrappers.<Topic>query().eq("type",type).orderByDesc("create_time"));

            topics = page.getRecords();
            list = topics.stream().map(this::Topic2Preview).toList();

            //放进缓存
            cacheUtils.saveListToCache(key,list,60);
        }

        return list;

    }

    //查询文章的收藏列表
    @Override
    public List<TopicPreviewVo> listTopicCollects(int uid) {

        return this.baseMapper.topicCollects(uid)
                .stream()
                .map(topic -> {
                    TopicPreviewVo vo = new TopicPreviewVo();
                    BeanUtils.copyProperties(topic,vo);
                    return vo;
                })
                .toList();

    }

    /**
     * 根据文章id 查询文章
     * @param tid
     * @return
     */
    @Override
    public TopicDetailVo getTopicDetailById(int tid,int uid) {

        TopicDetailVo vo  = new TopicDetailVo();

        Topic topic = getById(tid);
        BeanUtils.copyProperties(topic,vo);

        TopicDetailVo.User user = new TopicDetailVo.User();
        vo.setUser(this.fillUserDetailsByPrivacy(user,topic.getUid()));

        TopicDetailVo.Interact interact = new TopicDetailVo.Interact(
                hasInteract(tid,uid,"like"),
                hasInteract(tid,uid,"collect")
        );
        vo.setInteract(interact);
        vo.setComments(commentMapper.selectCount(Wrappers.<TopicComment>query().eq("tid",tid)));
        return vo;
    }

    @Override
    public void interact(Interact interact, boolean state) {
       String type = interact.getType();
        synchronized (type.intern())
        {
            stringRedisTemplate.opsForHash().put(type,interact.getKey(),Boolean.toString(state));
            this.saveInteractSchedule(type);
        }
    }

    @Override
    public String updateTopic(int uid, TopicUpdateVo vo) {

        if(!textLimitCheck(vo.getContent(),300)) return "文章内容超出限制，发文失败";
        if(!types.contains(vo.getType())) return "文章类型非法";

        baseMapper.update(null,Wrappers.<Topic>update()
                .eq("uid",uid)
                .eq("id",vo.getId())
                .set("title",vo.getTitle())
                .set("content",vo.getContent())
                .set("type",vo.getType()));
        return null;

    }


    //创建评论
    @Override
    public String createComment(int uid, AddCommentVo vo) {
        String key = Const.FORUM_TOPIC_COMMENT_COUNTER+":"+uid;
//        if(!flowUtils.limitPeriodCheck(key,60,60))//
//        {
//            return "评论频繁，请稍后重试";
//        }

        if(!textLimitCheck(JSONObject.parseObject(vo.getContent()),500))
            return "评论长度超限";

        TopicComment comment = new TopicComment();
        comment.setUid(uid);
        BeanUtils.copyProperties(vo,comment);
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);

        return null;

    }

    @Override
    public List<CommentVo> getComments(int tid, int pageNum) {
        Page<TopicComment> page = Page.of(pageNum,10);

        commentMapper.selectPage(page,Wrappers.<TopicComment>query().eq("tid",tid));

        return page.getRecords()
                .stream()
                .map(dto -> {
                    CommentVo vo = new CommentVo();
                    BeanUtils.copyProperties(dto,vo);

                    if(dto.getQuote()>0)
                    {
                        TopicComment comment = commentMapper.selectOne(Wrappers
                                .<TopicComment>query()
                                .eq("id", dto.getQuote())
                                .orderByAsc("create_time"));
                        if(comment!=null)
                        {
                            JSONObject object = JSONObject.parseObject(comment.getContent());

                            StringBuilder builder = new StringBuilder();

                            this.shortContent(object.getJSONArray("ops"), builder, ignore-> { });

                            vo.setQuote(builder.toString());
                        }
                        else vo.setQuote("此评论已被删除");

                    }
                    CommentVo.User user = new CommentVo.User();
                    this.fillUserDetailsByPrivacy(user,dto.getUid());
                    vo.setUser(user);
                    return vo;
                }).toList();


    }

    @Override
    public void deleteComment(int commentId, int uid) {

        commentMapper.delete(Wrappers.<TopicComment>query().eq("id",commentId).eq("uid",uid));
    }

    //判断是否有这个点赞或收藏
    private boolean hasInteract(int tid,int uid,String type)
    {
        String key = tid +":"+uid;
        //如果缓存中存在
        if(stringRedisTemplate.opsForHash().hasKey(type,key))
            return Boolean.parseBoolean(stringRedisTemplate.opsForHash().entries(type).get(key).toString());

        //缓存没有就看数据库有没有
        return baseMapper.userInteractCount(tid,uid,type)>0;

    }

    private final Map<String,Boolean> state = new HashMap<>();

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    private void saveInteractSchedule(String type)
    {
        if(!state.getOrDefault(type,false))
        {
            state.put(type,true);
            scheduledExecutorService.schedule(()->{
                this.saveInteract(type);
                state.put(type,false);
            },3, TimeUnit.SECONDS);
        }
    }


    //入库
    private void saveInteract(String type)
    {

        synchronized (type.intern())
        {
            List<Interact> check = new LinkedList<>();
            List<Interact> uncheck = new LinkedList<>();

            stringRedisTemplate.opsForHash().entries(type).forEach((k,v)->{
                if(Boolean.parseBoolean(v.toString()))
                {
                    check.add(Interact.parseInteract(k.toString(),type));
                }
                else  uncheck.add(Interact.parseInteract(k.toString(),type));

            });
            if(!check.isEmpty()) baseMapper.addInteract(check,type);
            if(!uncheck.isEmpty()) baseMapper.deleteInteract(uncheck,type);

            stringRedisTemplate.delete(type);
        }

    }

    //根据用户的隐私设置有选择地填充信息
    private <T> T fillUserDetailsByPrivacy(T target,int uid)
    {
        Account account = accountMapper.selectById(uid);
        AccountDetails accountDetails = accountDetailsService.findAccountDetailsById(uid);

        AccountPrivacy privacy = accountPrivacyMapper.selectById(uid);

        String[] ignores = privacy.hiddenFields();

        BeanUtils.copyProperties(account,target,ignores);

        BeanUtils.copyProperties(accountDetails,target,ignores);

        return target;

    }

    /**
     * 将Topic转为TopicPreviewVO
     */
    private TopicPreviewVo Topic2Preview(Topic topic)
    {
        TopicPreviewVo vo = new TopicPreviewVo();

        BeanUtils.copyProperties(accountMapper.selectById(topic.getUid()),vo);
        BeanUtils.copyProperties(topic,vo);

        List<String> images = new ArrayList<>();

        StringBuilder previewText = new StringBuilder();

        JSONArray ops = JSONObject.parseObject(topic.getContent()).getJSONArray("ops");

//        for(Object op :ops) {
//            Object insert = JSONObject.from(op).get("insert");
//
//            if (insert instanceof String text) {
//                if (previewText.length() >= 300) continue;
//                previewText.append(text);
//            } else if (insert instanceof Map<?, ?> map) {
//                Optional.ofNullable(map.get("image"))
//                        .ifPresent(obj -> images.add(obj.toString()));
//            }
//        }
        this.shortContent(ops,previewText,obj -> images.add(obj.toString()));

        vo.setText(previewText.length()>300 ? previewText.substring(0,300) : previewText.toString());
        vo.setImages(images);
        return vo;
    }


    //将文章 弄成简短的预览
    private void shortContent(JSONArray ops, StringBuilder previewText, Consumer<Object> imageHandler)
    {
        for(Object op :ops) {
            Object insert = JSONObject.from(op).get("insert");

            if (insert instanceof String text) {
                if (previewText.length() >= 300) continue;
                previewText.append(text);
            } else if (insert instanceof Map<?, ?> map) {
                Optional.ofNullable(map.get("image"))
                        .ifPresent(imageHandler);
            }
        }
    }


    /**
     *
     * @param object 限制对象
     * @param limit 限制长度
     * @return 是否通过检测
     */
    private boolean textLimitCheck(JSONObject object,int limit)
    {
        if(object==null) return false;

        long length = 0;

        for(Object op : object.getJSONArray("ops"))
        {
            length +=JSONObject.from(op).getString("insert").length();
            if(length>limit) return false;
        }
        return true;
    }


}

