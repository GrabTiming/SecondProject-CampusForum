package com.example.entity.vo.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DetailsSaveVo {

    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    String username;

    @Min(1)
    @Max(2)
    int gender;

    @Length(max=11)
    String phone;

    @Length(max=13)
    String qq;

    @Length(max=20)
    String wx;
    @Length(max=300)
    String description;



}
