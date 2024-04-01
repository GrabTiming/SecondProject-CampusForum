package com.example.entity.vo.request;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicUpdateVo {

    @Min(0)
    private Integer id;
    @Min(1)
    @Max(5)
    private Integer type;

    @Length(min=1,max=50)
    private String title;

    private JSONObject content;

}
