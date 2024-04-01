package com.example.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountVo {

    private String username;
    private String email;
    private String role;
    private Date registerTime;
    private String avatar;

}
