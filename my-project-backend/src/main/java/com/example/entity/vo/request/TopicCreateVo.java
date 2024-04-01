package com.example.entity.vo.request;


import com.alibaba.fastjson2.JSONObject;
import com.google.gson.JsonObject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class TopicCreateVo {

    @Min(1)
    @Max(5)
    private Integer type;

    @Length(min=1,max=50)
    private String title;

    private JSONObject content;

}
