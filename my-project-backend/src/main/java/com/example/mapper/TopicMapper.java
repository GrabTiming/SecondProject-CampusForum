package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Interact;
import com.example.entity.dto.Topic;
import com.example.entity.vo.response.TopicPreviewVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (Topic)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-29 16:14:40
 */
@Mapper
public interface TopicMapper extends BaseMapper<Topic> {


    @Insert("""
               <script> 
                    insert ignore into db_topic_interact_${type} 
                    values 
                    <foreach collection  = "interacts" item = "item" separator = ",">
                        (#{item.uid},#{item.tid},#{item.time})
                    </foreach>
               </script>
            """)
    void addInteract(List<Interact> interacts,String type);


    @Insert("""
               <script> 
                    delete from db_topic_interact_${type} 
                    where
                    <foreach collection  = "interacts" item = "item" separator = " or ">
                        (uid = #{item.uid} and tid = #{item.tid})
                    </foreach>
               </script>
            """)
    void deleteInteract(List<Interact> interacts,String type);


    //查询文章对于类型type的次数
    @Select("""
            select count(*) from db_topic_interact_${type} where tid = #{tid};
            """)
    int interactCount(int tid,String type);

    @Select("""
            select count(*) from db_topic_interact_${type} where tid = #{tid} and uid = #{uid};
            """)
    int userInteractCount(int tid,int uid,String type);

    @Select("""
            select * from db_topic_interact_collect C left join db_topic T on C.tid=T.id
            where  C.uid = #{uid};
            """)
    List<Topic> topicCollects(int uid) ;

}

