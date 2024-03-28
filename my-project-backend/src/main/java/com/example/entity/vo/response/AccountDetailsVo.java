package com.example.entity.vo.response;

import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailsVo  {

    int gender;
    String phone;
    String qq;
    String wx;
    String description;
}
