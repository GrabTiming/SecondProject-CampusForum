package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;

@Data
@TableName("db_account_privacy")
public class AccountPrivacy implements BaseData {

    @TableId(type = IdType.AUTO)
    private final Integer id;

    private boolean phone  = true;

    private boolean email =true;

    private boolean wx = true;

    private boolean qq = true;

    private boolean gender = true;
}
