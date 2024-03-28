package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class EmailModifyVo {

    @Email
    String email;

    @Min(6)
    @Max(6)
    int code;

}
