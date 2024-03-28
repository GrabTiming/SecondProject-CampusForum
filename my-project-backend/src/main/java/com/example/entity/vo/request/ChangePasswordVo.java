package com.example.entity.vo.request;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChangePasswordVo {

    @Length(min=6,max=30)
    String password;
    @Length(min=6,max=30)
    String new_password;

}
