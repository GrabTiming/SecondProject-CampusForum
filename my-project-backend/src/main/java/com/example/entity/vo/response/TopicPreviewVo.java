package com.example.entity.vo.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicPreviewVo {

    private Integer id;

    private Integer type;

    private String title;

    private String text;

    private List<String> images;

    private Date createTime;

    private Integer uid;

    private String username;

    private String avatar;

}
